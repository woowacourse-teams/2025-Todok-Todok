import com.team.domain.model.member.MemberType
import com.team.todoktodok.data.core.JwtParser
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.Base64

class JwtParserTest {
    private fun createJwt(
        role: String,
        memberId: Long,
    ): String {
        val header =
            Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString("""{"alg":"HS256","typ":"JWT"}""".toByteArray())

        val payloadJson =
            JSONObject()
                .put("role", role)
                .put("sub", memberId)
                .toString()

        val payload =
            Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(payloadJson.toByteArray())

        val signature = "signature"

        return "$header.$payload.$signature"
    }

    @Test
    fun `parseToMemberType과_parseToMemberId가_정상적으로_값을_추출한다`() {
        // given
        val jwt = createJwt("USER", 123L)

        // when
        val parser = JwtParser(jwt)

        // then
        assertEquals(MemberType.USER, parser.parseToMemberType())
        assertEquals(123L, parser.parseToMemberId())
    }

    @Test
    fun `JWT_파트_개수가_3개가_아니면_예외_발생`() {
        // given
        val invalidJwt = "invalid.token"

        // then
        assertThrows(IllegalArgumentException::class.java) {
            JwtParser(invalidJwt).parseToMemberType()
        }
    }
}
