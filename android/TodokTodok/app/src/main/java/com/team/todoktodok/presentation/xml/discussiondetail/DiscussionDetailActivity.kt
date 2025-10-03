package com.team.todoktodok.presentation.xml.discussiondetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import androidx.fragment.app.commit
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityDiscussionDetailBinding
import com.team.todoktodok.databinding.MenuExternalDiscussionBinding
import com.team.todoktodok.databinding.MenuOwnedDiscussionBinding
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionResult.Companion.EXTRA_DELETE_DISCUSSION
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionResult.Companion.EXTRA_WATCHED_DISCUSSION
import com.team.todoktodok.presentation.compose.main.MainActivity
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.core.component.CommonDialog
import com.team.todoktodok.presentation.core.component.ReportDialog
import com.team.todoktodok.presentation.core.ext.extractSubtitle
import com.team.todoktodok.presentation.core.ext.getParcelableCompat
import com.team.todoktodok.presentation.core.ext.loadCircleImage
import com.team.todoktodok.presentation.core.ext.loadImage
import com.team.todoktodok.presentation.core.ext.registerPositiveResultListener
import com.team.todoktodok.presentation.core.ext.registerReportResultListener
import com.team.todoktodok.presentation.core.ext.toRelativeString
import com.team.todoktodok.presentation.core.utils.shareDiscussionLink
import com.team.todoktodok.presentation.xml.auth.AuthActivity
import com.team.todoktodok.presentation.xml.discussion.create.CreateDiscussionRoomActivity
import com.team.todoktodok.presentation.xml.discussion.create.SerializationCreateDiscussionRoomMode
import com.team.todoktodok.presentation.xml.discussiondetail.comments.CommentsFragment
import com.team.todoktodok.presentation.xml.discussiondetail.vm.DiscussionDetailViewModel
import com.team.todoktodok.presentation.xml.discussiondetail.vm.DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID
import com.team.todoktodok.presentation.xml.discussiondetail.vm.DiscussionDetailViewModel.Companion.KEY_MODE
import com.team.todoktodok.presentation.xml.discussiondetail.vm.DiscussionDetailViewModelFactory
import com.team.todoktodok.presentation.xml.profile.ProfileActivity
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion

class DiscussionDetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DiscussionDetailViewModel> {
        val repositoryModule = (application as App).container.repositoryModule
        DiscussionDetailViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.tokenRepository,
        )
    }
    private val binding: ActivityDiscussionDetailBinding by lazy {
        ActivityDiscussionDetailBinding.inflate(
            layoutInflater,
        )
    }

    private var popupWindow: PopupWindow? = null

    private val messageConverter: ExceptionMessageConverter by lazy {
        ExceptionMessageConverter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initView()
        setContentView(binding.root)
        setupOnClick()
        setupObserve()
        setUpRefresh()
        handleIntent(this.intent)
        setUpDialogResultListener()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        when (val entryPoint = parseEntryPoint(intent)) {
            is DiscussionEntryPoint.Standard -> {
                entryPoint.mode?.let { viewModel.fetchMode(it) }
                viewModel.initLoadDiscission(entryPoint.discussionId)
                showComments(entryPoint.discussionId)
            }

            is DiscussionEntryPoint.FromDeepLink -> {
                viewModel.initLoadDiscission(entryPoint.discussionId)
                showComments(entryPoint.discussionId)
            }

            is DiscussionEntryPoint.Invalid -> {
                Toast
                    .makeText(
                        this,
                        getString(R.string.discussion_entry_invalid_message),
                        Toast.LENGTH_SHORT,
                    ).show()
                finish()
            }
        }
    }

    private fun parseEntryPoint(intent: Intent): DiscussionEntryPoint {
        if (intent.data != null) {
            val id = intent.data?.lastPathSegment?.toLongOrNull()
            return if (id != null) {
                DiscussionEntryPoint.FromDeepLink(id)
            } else {
                DiscussionEntryPoint.Invalid
            }
        }

        val id = intent.getLongExtra(KEY_DISCUSSION_ID, NOT_FOUND_DISCUSSION_ID)
        if (id != NOT_FOUND_DISCUSSION_ID) {
            val mode = intent.getParcelableCompat<SerializationCreateDiscussionRoomMode>(KEY_MODE)
            return DiscussionEntryPoint.Standard(id, mode)
        }

        return DiscussionEntryPoint.Invalid
    }

    override fun onDestroy() {
        popupWindow?.dismiss()
        popupWindow = null
        super.onDestroy()
    }

    private fun initView() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                binding.root.paddingLeft,
                binding.root.marginTop,
                binding.root.paddingRight,
                systemBars.bottom,
            )
            insets
        }
    }

    private fun setUpRefresh() {
        binding.srlDiscussionContainer.setOnRefreshListener {
            viewModel.reloadDiscussion()
        }
    }

    private fun setupOnClick() {
        val sheetView = binding.bottomSheetContainer
        val behavior = BottomSheetBehavior.from(sheetView)
        with(binding) {
            ivDiscussionDetailBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            ivComment.setOnClickListener {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            tvCommentCount.setOnClickListener {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            ivUserProfile.setOnClickListener {
                viewModel.navigateToProfile()
            }
            tvUserNickname.setOnClickListener {
                viewModel.navigateToProfile()
            }
            ivDiscussionShare.setOnClickListener {
                viewModel.uiState.value?.discussion?.let { discussion ->
                    this@DiscussionDetailActivity.shareDiscussionLink(
                        discussion.id,
                        discussion.discussionTitle,
                    )
                }
            }
            tvDiscussionOpinion.setOnClickListener {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            setupLikeClick()
        }
    }

    private fun setupLikeClick() {
        with(binding) {
            ivLike.setOnClickListener {
                viewModel.toggleLike()
            }
            tvLikeCount.setOnClickListener {
                viewModel.toggleLike()
            }
        }
    }

    private fun setupPopUpDiscussionClick(isMyDiscussion: Boolean) {
        binding.ivDiscussionOption.setOnClickListener {
            if (popupWindow == null) popupWindow = getPopUpView(isMyDiscussion)
            if (popupWindow?.isShowing == true) {
                popupWindow?.dismiss()
            } else {
                popupWindow?.showAsDropDown(it)
            }
        }
    }

    private fun getPopUpView(isMyDiscussion: Boolean): PopupWindow =
        if (isMyDiscussion) {
            val binding = MenuOwnedDiscussionBinding.inflate(layoutInflater)
            binding.tvEdit.setOnClickListener {
                viewModel.updateDiscussion()
                popupWindow?.dismiss()
            }
            binding.tvDelete.setOnClickListener {
                showDeleteDialog()
                popupWindow?.dismiss()
            }
            createPopUpView(binding.root)
        } else {
            val binding = MenuExternalDiscussionBinding.inflate(layoutInflater)
            binding.tvReport.setOnClickListener {
                showReportDialog()
                popupWindow?.dismiss()
            }
            createPopUpView(binding.root)
        }

    private fun showDeleteDialog() {
        val dialog =
            CommonDialog.newInstance(
                getString(R.string.all_discussion_delete_confirm),
                getString(R.string.all_delete_action),
                DISCUSSION_DELETE_DIALOG_REQUEST_KEY,
            )
        dialog.show(supportFragmentManager, CommonDialog.TAG)
    }

    private fun showReportDialog() {
        val dialog =
            ReportDialog.newInstance(
                DISCUSSION_REPORT_DIALOG_REQUEST_KEY,
            )
        dialog.show(supportFragmentManager, CommonDialog.TAG)
    }

    private fun createPopUpView(popupView: View) =
        PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true,
        )

    private fun setupObserve() {
        viewModel.uiState.observe(this) { value ->
            with(binding) {
                if (value != null) {
                    if (value.isLoading) {
                        progressBar.show()
                    } else {
                        progressBar.hide()
                        val discussion = value.discussion
                        tvBookTitle.text = discussion.book.title.extractSubtitle()
                        tvDiscussionTitle.text = discussion.discussionTitle
                        tvUserNickname.text = discussion.writer.nickname.value
                        ivUserProfile.loadCircleImage(discussion.writer.profileImage)
                        ivBookImage.loadImage(discussion.book.image)
                        tvDiscussionCreateAt.text =
                            discussion.createAt.toRelativeString(this@DiscussionDetailActivity)
                        tvDiscussionOpinion.text = discussion.discussionOpinion
                        ivLike.isSelected = discussion.isLikedByMe
                        tvLikeCount.text = discussion.likeCount.toString()
                        tvViewsCount.text = discussion.viewCount.toString()
                        tvCommentCount.text = discussion.commentCount.toString()
                    }
                    setupPopUpDiscussionClick(value.isMyDiscussion)
                }
            }
        }
        viewModel.uiEvent.observe(this) { value ->
            handleEvent(value)
        }
    }

    private fun handleEvent(event: DiscussionDetailUiEvent) {
        when (event) {
            is DiscussionDetailUiEvent.DeleteDiscussion ->
                moveToDiscussionsWithDeletedDiscussionId(event.discussionId)

            is DiscussionDetailUiEvent.UpdateDiscussion ->
                moveToCreateDiscussion(event.discussionId)

            is DiscussionDetailUiEvent.NavigateToProfile ->
                moveToProfile(memberId = event.userId)

            is DiscussionDetailUiEvent.ShowErrorMessage ->
                showSnackBar(messageConverter(event.exceptions))

            DiscussionDetailUiEvent.ShowReportDiscussionSuccessMessage ->
                showSnackBar(R.string.all_report_discussion_success)

            is DiscussionDetailUiEvent.NavigateToDiscussionsWithResult -> {
                moveToDiscussionsWithResult(event.mode, event.discussion)
            }

            DiscussionDetailUiEvent.ReloadedDiscussion -> {
                binding.srlDiscussionContainer.isRefreshing = false
            }

            is DiscussionDetailUiEvent.NotFoundDiscussion -> {
                Toast.makeText(this, messageConverter(event.exceptions), Toast.LENGTH_SHORT).show()
                val intent =
                    DiscussionsActivity.Intent(this).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                startActivity(intent)
            }

            is DiscussionDetailUiEvent.Unauthorized -> {
                Toast.makeText(this, messageConverter(event.exceptions), Toast.LENGTH_SHORT).show()
                val intent =
                    AuthActivity.Intent(this).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                startActivity(intent)
            }
        }
    }

    private fun moveToDiscussionsWithDeletedDiscussionId(discussionId: Long) {
        val resultIntent = Intent().putExtra(EXTRA_DELETE_DISCUSSION, discussionId)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun moveToDiscussionsWithResult(
        mode: SerializationCreateDiscussionRoomMode?,
        discussion: SerializationDiscussion,
    ) {
        when (mode) {
            is SerializationCreateDiscussionRoomMode.Create -> {
                val intent =
                    MainActivity.Intent(this).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                startActivity(intent)
                finish()
            }

            else -> {
                val resultIntent =
                    Intent().apply {
                        putExtra(EXTRA_WATCHED_DISCUSSION, discussion)
                    }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun moveToCreateDiscussion(discussionId: Long) {
        val intent =
            CreateDiscussionRoomActivity.Intent(
                this@DiscussionDetailActivity,
                SerializationCreateDiscussionRoomMode.Edit(discussionId),
            )
        startActivity(intent)
    }

    private fun moveToProfile(memberId: Long) {
        val intent = ProfileActivity.Intent(this, memberId)
        startActivity(intent)
    }

    private fun showSnackBar(
        @StringRes message: Int,
    ) {
        AlertSnackBar(binding.root, message).show()
    }

    private fun showComments(discussionId: Long) {
        val sheetView = binding.bottomSheetContainer
        val behavior = BottomSheetBehavior.from(sheetView)

        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        behavior.setPeekHeight(
            resources.getDimensionPixelSize(R.dimen.item_discussion_detail_bottom_sheet_min_height),
            true,
        )
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                binding.bottomSheetContainer.id,
                CommentsFragment.newInstance(
                    discussionId,
                ),
            )
        }
    }

    private fun setUpDialogResultListener() {
        supportFragmentManager.registerReportResultListener(
            this,
            DISCUSSION_REPORT_DIALOG_REQUEST_KEY,
            ReportDialog.RESULT_KEY_REPORT,
        ) { reason ->
            viewModel.reportDiscussion(reason)
        }

        supportFragmentManager.registerPositiveResultListener(
            this,
            DISCUSSION_DELETE_DIALOG_REQUEST_KEY,
            CommonDialog.RESULT_KEY_COMMON_DIALOG,
        ) {
            viewModel.deleteDiscussion()
        }
    }

    companion object {
        private const val DISCUSSION_DELETE_DIALOG_REQUEST_KEY =
            "discussion_delete_dialog_request_key"
        private const val DISCUSSION_REPORT_DIALOG_REQUEST_KEY =
            "discussion_report_dialog_request_key"
        private const val NOT_FOUND_DISCUSSION_ID = -1L

        fun Intent(
            context: Context,
            discussionId: Long,
            mode: SerializationCreateDiscussionRoomMode? = null,
        ): Intent =
            Intent(context, DiscussionDetailActivity::class.java).apply {
                putExtra(KEY_DISCUSSION_ID, discussionId)
                mode?.let { putExtra(KEY_MODE, mode) }
            }
    }
}
