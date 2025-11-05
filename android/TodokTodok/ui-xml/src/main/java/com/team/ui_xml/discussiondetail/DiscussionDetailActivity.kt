package com.team.ui_xml.discussiondetail

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.team.core.ExceptionMessageConverter
import com.team.core.extension.getParcelableCompat
import com.team.core.extension.registerPositiveResultListener
import com.team.core.extension.registerReportResultListener
import com.team.core.navigation.BookDiscussionsRoute
import com.team.core.navigation.MainRoute
import com.team.ui_xml.R
import com.team.ui_xml.auth.AuthActivity
import com.team.ui_xml.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.ui_xml.component.CommonDialog
import com.team.ui_xml.component.ReportDialog
import com.team.ui_xml.create.CreateDiscussionRoomActivity
import com.team.ui_xml.create.SerializationCreateDiscussionRoomMode
import com.team.ui_xml.databinding.ActivityDiscussionDetailBinding
import com.team.ui_xml.databinding.MenuExternalDiscussionBinding
import com.team.ui_xml.databinding.MenuOwnedDiscussionBinding
import com.team.ui_xml.discussiondetail.comments.CommentsFragment
import com.team.ui_xml.discussiondetail.vm.DiscussionDetailViewModel
import com.team.ui_xml.extension.loadCircleImage
import com.team.ui_xml.extension.loadImage
import com.team.ui_xml.extension.toRelativeString
import com.team.ui_xml.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DiscussionDetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DiscussionDetailViewModel>()
    private val binding: ActivityDiscussionDetailBinding by lazy {
        ActivityDiscussionDetailBinding.inflate(
            layoutInflater,
        )
    }

    private var popupWindow: PopupWindow? = null

    @Inject
    lateinit var messageConverter: ExceptionMessageConverter

    @Inject
    lateinit var mainNavigation: MainRoute

    @Inject
    lateinit var bookDiscussionsNavigation: BookDiscussionsRoute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        initView()
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

        val id = intent.getLongExtra(DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID, NOT_FOUND_DISCUSSION_ID)
        if (id != NOT_FOUND_DISCUSSION_ID) {
            val mode =
                intent.getParcelableCompat<SerializationCreateDiscussionRoomMode>(
                    DiscussionDetailViewModel.Companion.KEY_MODE,
                )
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
        ViewCompat.setOnApplyWindowInsetsListener(binding.srlDiscussionContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                binding.srlDiscussionContainer.paddingLeft,
                systemBars.top,
                binding.srlDiscussionContainer.paddingRight,
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
                viewModel.navigateToOtherUserProfile()
            }
            tvUserNickname.setOnClickListener {
                viewModel.navigateToOtherUserProfile()
            }
            ivDiscussionShare.setOnClickListener {
                viewModel.shareDiscussion()
            }
            tvDiscussionOpinion.setOnClickListener {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            ivBookImage.setOnClickListener {
                viewModel.navigateToBookDiscussion()
            }

            tvBookTitle.setOnClickListener {
                viewModel.navigateToBookDiscussion()
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
                when (value) {
                    DiscussionDetailUiState.Empty -> progressBar.show()
                    is DiscussionDetailUiState.Success -> {
                        if (progressBar.isVisible) progressBar.hide()
                        val discussion = value.discussion
                        tvBookTitle.text = discussion.book.extractSubtitle()
                        tvDiscussionTitle.text = discussion.discussionTitle
                        tvUserNickname.text = discussion.writer.nickname
                        ivUserProfile.loadCircleImage(discussion.writer.profileImage)
                        ivBookImage.loadImage(discussion.book.image)
                        tvDiscussionCreateAt.text =
                            discussion.createAt.toRelativeString(this@DiscussionDetailActivity)
                        tvDiscussionOpinion.text = discussion.discussionOpinion
                        ivLike.isSelected = discussion.isLikedByMe
                        tvLikeCount.text = discussion.likeCount.toString()
                        tvViewsCount.text = discussion.viewCount.toString()
                        tvCommentCount.text = discussion.commentCount.toString()
                        setupPopUpDiscussionClick(value.isMyDiscussion)
                    }
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
                moveToDiscussionsWithResult(event.mode)
            }

            DiscussionDetailUiEvent.ReloadedDiscussion -> {
                binding.srlDiscussionContainer.isRefreshing = false
            }

            is DiscussionDetailUiEvent.NotFoundDiscussion -> {
                Toast.makeText(this, messageConverter(event.exceptions), Toast.LENGTH_SHORT).show()
                mainNavigation.navigateToMain(this) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }

            is DiscussionDetailUiEvent.Unauthorized -> {
                Toast.makeText(this, messageConverter(event.exceptions), Toast.LENGTH_SHORT).show()
                val intent =
                    AuthActivity.Intent(this).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                startActivity(intent)
            }

            is DiscussionDetailUiEvent.ShareDiscussion -> {
                shareDiscussionLink(
                    event.discussionId,
                    event.discussionTitle,
                )
            }

            is DiscussionDetailUiEvent.NavigateToBookDiscussions -> {
                bookDiscussionsNavigation.navigateToBookDiscussions(this, event.bookId)
            }
        }
    }

    private fun moveToDiscussionsWithDeletedDiscussionId(discussionId: Long) {
        setResult(RESULT_OK)
        finish()
    }

    private fun moveToDiscussionsWithResult(mode: SerializationCreateDiscussionRoomMode?) {
        when (mode) {
            is SerializationCreateDiscussionRoomMode.Create -> {
                mainNavigation.navigateToMain(this) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }

            else -> {
                setResult(RESULT_OK)
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
        val intent = ProfileActivity.Companion.Intent(this, memberId)
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

        behavior.isHideable = false
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
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

    fun shareDiscussionLink(
        id: Long,
        title: String,
    ) {
        val url =
            Uri
                .Builder()
                .scheme(HOST)
                .authority(SCHEME)
                .appendPath(PATH_DISCUSSION)
                .appendPath(id.toString())
                .build()
                .toString()

        val urlMessage = "$title\n$url"
        val intent =
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.discussion_link_title))
                putExtra(Intent.EXTRA_TEXT, urlMessage)
            }
        startActivity(Intent.createChooser(intent, getString(R.string.share_discussion_title)))
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
        private const val HOST = "todoktodok.com"
        private const val SCHEME = "https"
        private const val PATH_DISCUSSION = "discussiondetail"
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
                putExtra(DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID, discussionId)
                mode?.let { putExtra(DiscussionDetailViewModel.Companion.KEY_MODE, mode) }
            }
    }
}
