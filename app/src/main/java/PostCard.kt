
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────
// Tokens
// ─────────────────────────────────────────────
private val ColorSurface         = Color(0xFFFFFFFF)
private val ColorNeutralContainer= Color(0xFFEBEBEB)
private val ColorOnNeutralContainer = Color(0xFF959595)
private val ColorDeadlineBg      = Color(0xFFFFDADA)
private val ColorDeadlineText    = Color.Red
private val ColorJobTitle        = Color(0xFF262626)
private val ColorCompanyName     = Color(0xFF7C7C7C)
private val ColorAppliedBg       = Color(0xFFFDC307)
private val CardShape            = RoundedCornerShape(12.dp)
private val ChipShape            = RoundedCornerShape(19.dp)
private val DeadlineShape        = RoundedCornerShape(10.dp)
private val LogoShape            = RoundedCornerShape(9.dp)

// ─────────────────────────────────────────────
// Chip
// ─────────────────────────────────────────────
@Composable
private fun Chip(
    label: String,
    modifier: Modifier = Modifier,
    iconContent: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .height(32.dp)
            .clip(ChipShape)
            .background(ColorNeutralContainer)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (iconContent != null) Arrangement.spacedBy(6.dp) else Arrangement.Center,
    ) {
        iconContent?.invoke()
        Text(
            text = label,
            style = TextStyle(
                color = ColorOnNeutralContainer,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.12.sp,
            ),
            maxLines = 1,
        )
    }
}

// ─────────────────────────────────────────────
// Deadline Badge
// ─────────────────────────────────────────────
@Composable
private fun DeadlineBadge(
    deadline: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .width(80.dp)
            .clip(DeadlineShape)
            .background(ColorDeadlineBg)
            .padding(horizontal = 7.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        // Red dot
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(RoundedCornerShape(50))
                .background(ColorDeadlineText),
        )
        Text(
            text = deadline,
            style = TextStyle(
                color = ColorDeadlineText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.13.sp,
            ),
            maxLines = 1,
        )
    }
}

// ─────────────────────────────────────────────
// Card body (shared between both states)
// ─────────────────────────────────────────────
@Composable
private fun CardBody(
    jobProfile: String,
    companyName: String,
    deadline: String,
    salary: String,
    groupLabel: String,
    cityLabel: String,
    applicantsLabel: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(23.dp),
    ) {
        // ── Header row ──────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                // Company logo placeholder — drawn with Canvas, no extra deps
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(LogoShape)
                        .background(ColorNeutralContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Canvas(modifier = Modifier.size(22.dp)) {
                        val stroke = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
                        val color = Color(0xFF959595)
                        val w = size.width
                        val h = size.height

                        // Outer rounded rectangle (frame)
                        drawRoundRect(
                            color = color,
                            style = stroke,
                            cornerRadius = CornerRadius(2.dp.toPx()),
                        )

                        // Mountain / landscape triangle
                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(0f, h * 0.75f)
                            lineTo(w * 0.38f, h * 0.38f)
                            lineTo(w * 0.62f, h * 0.58f)
                            lineTo(w * 0.78f, h * 0.42f)
                            lineTo(w, h * 0.65f)
                        }
                        drawPath(path = path, color = color, style = stroke)

                        // Sun circle
                        drawCircle(
                            color = color,
                            radius = w * 0.12f,
                            center = Offset(w * 0.78f, h * 0.28f),
                            style = stroke,
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = jobProfile,
                        style = TextStyle(
                            color = ColorJobTitle,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.16.sp,
                            lineHeight = 20.sp,
                        ),
                    )
                    Text(
                        text = companyName,
                        style = TextStyle(
                            color = ColorCompanyName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            letterSpacing = 0.13.sp,
                            lineHeight = 20.sp,
                        ),
                    )
                }
            }

            DeadlineBadge(deadline = deadline)
        }

        // ── Chips row ────────────────────────────────
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Group chip — layered-circles icon drawn with Canvas
            Chip(
                label = groupLabel,
                iconContent = {
                    Canvas(modifier = Modifier.size(14.dp)) {
                        val c = Color(0xFF959595)
                        val r = size.minDimension / 2f
                        val stroke = Stroke(1.4.dp.toPx())
                        // Three stacked ellipses to represent "layers/group"
                        drawOval(color = c, topLeft = Offset(size.width * 0.1f, size.height * 0.55f),
                            size = Size(size.width * 0.8f, size.height * 0.28f), style = stroke)
                        drawOval(color = c, topLeft = Offset(size.width * 0.1f, size.height * 0.3f),
                            size = Size(size.width * 0.8f, size.height * 0.28f), style = stroke)
                        drawOval(color = c, topLeft = Offset(size.width * 0.1f, size.height * 0.05f),
                            size = Size(size.width * 0.8f, size.height * 0.28f), style = stroke)
                    }
                },
            )

            // City chip — pin/location icon drawn with Canvas
            Chip(
                label = cityLabel,
                iconContent = {
                    Canvas(modifier = Modifier.size(12.dp, 15.dp)) {
                        val c = Color(0xFF959595)
                        val stroke = Stroke(1.4.dp.toPx(), cap = StrokeCap.Round)
                        val cx = size.width / 2f
                        val r = size.width * 0.42f
                        // Circle top
                        drawCircle(color = c, radius = r, center = Offset(cx, r + 1.dp.toPx()), style = stroke)
                        // Teardrop tail drawn as a simple line
                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(cx - r * 0.6f, r * 1.6f + 1.dp.toPx())
                            quadraticBezierTo(cx, size.height + 1.dp.toPx(), cx + r * 0.6f, r * 1.6f + 1.dp.toPx())
                        }
                        drawPath(path, color = c, style = stroke)
                    }
                },
            )

            Chip(label = applicantsLabel)
        }

        // ── Salary row ───────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.18.sp,
                        )
                    ) { append(salary) }
                },
                style = TextStyle(
                    color = Color.Black,
                    lineHeight = 20.sp,
                ),
            )

            // Chevron arrow — Canvas drawn, no drawable needed
            Canvas(modifier = Modifier.size(10.dp, 18.dp)) {
                val c = Color(0xFF444444)
                val stroke = Stroke(1.8.dp.toPx(), cap = StrokeCap.Round)
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, size.height / 2f)
                    lineTo(0f, size.height)
                }
                drawPath(path, color = c, style = stroke)
            }
        }
    }
}

// ─────────────────────────────────────────────
// Public composable
// ─────────────────────────────────────────────
/**
 * Job Post Card
 *
 * @param applied         When true shows the yellow "Applied" footer banner.
 * @param jobProfile      Title text, e.g. "Software Engineer".
 * @param companyName     Sub-title text, e.g. "Google".
 * @param deadline        Short deadline label shown in the red badge, e.g. "3 Days".
 * @param salary          Salary range string, e.g. "₹ 8,00,000 – ₹ 12,00,000".
 * @param groupLabel      Label for the first chip (role/group).
 * @param cityLabel       Label for the city chip.
 * @param applicantsLabel Label for the applicants chip.
 */
@Composable
fun JobPostCard(
    modifier: Modifier = Modifier,
    applied: Boolean = false,
    jobProfile: String = "Job Profile",
    companyName: String = "Company Name",
    deadline: String = "x Days",
    salary: String = "₹ X,XX,XXX – ₹ XX,XX,XX",
    groupLabel: String = "Group",
    cityLabel: String = "City",
    applicantsLabel: String = "xx Applicants",
) {
    Column(
        modifier = modifier
            .width(380.dp)
            .shadow(
                elevation = 12.dp,
                shape = CardShape,
                ambientColor = Color(0x1A000000),
                spotColor = Color(0x1A000000),
            )
            .clip(CardShape)
            .background(ColorSurface),
    ) {
        // Card body
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorSurface)
                .padding(18.dp),
        ) {
            CardBody(
                jobProfile = jobProfile,
                companyName = companyName,
                deadline = deadline,
                salary = salary,
                groupLabel = groupLabel,
                cityLabel = cityLabel,
                applicantsLabel = applicantsLabel,
            )
        }

        // Applied footer banner (only shown when applied = true)
        if (applied) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp)
                    .background(ColorAppliedBg)
                    .padding(horizontal = 21.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Checkmark — Canvas drawn, no drawable needed
                Canvas(modifier = Modifier.size(18.dp, 13.dp)) {
                    val stroke = Stroke(2.dp.toPx(), cap = StrokeCap.Round)
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(0f, size.height * 0.5f)
                        lineTo(size.width * 0.35f, size.height)
                        lineTo(size.width, 0f)
                    }
                    drawPath(path, color = Color.Black, style = stroke)
                }
                Text(
                    text = "Applied",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.13.sp,
                        lineHeight = 20.sp,
                    ),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
private fun JobPostCardPreview() {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // Not applied
        JobPostCard(
            applied = false,
            jobProfile = "Job Profile",
            companyName = "Company Name",
            deadline = "x Days",
            salary = "₹ X,XX,XXX – ₹ XX,XX,XX",
            groupLabel = "Group",
            cityLabel = "City",
            applicantsLabel = "xx Applicants",
        )

        // Applied
        JobPostCard(
            applied = true,
            jobProfile = "Job Profile",
            companyName = "Company Name",
            deadline = "x Days",
            salary = "₹ X,XX,XXX – ₹ XX,XX,XX",
            groupLabel = "Group",
            cityLabel = "City",
            applicantsLabel = "xx Applicants",
        )
    }
}