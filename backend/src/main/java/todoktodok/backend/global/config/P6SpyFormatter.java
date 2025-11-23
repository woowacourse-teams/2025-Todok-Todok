package todoktodok.backend.global.config;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.engine.jdbc.internal.FormatStyle;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class P6SpyFormatter implements MessageFormattingStrategy {

    private static final String NEW_LINE = "\n";
    private static final String TAP = "\t";
    private static final String CREATE = "create";
    private static final String ALTER = "alter";
    private static final String DROP = "drop";
    private static final String COMMENT = "comment";
    private final AtomicInteger queryCount = new AtomicInteger(0);

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (sql.trim().isEmpty()) {
            return formatByCommand(category);
        }
        queryCount.incrementAndGet();
        return formatBySql(sql, category) + getAdditionalMessages(elapsed);
    }

    private String formatByCommand(String category) {
        final int count = queryCount.getAndSet(0);
        return NEW_LINE
                + "Execute Command : "
                + NEW_LINE
                + NEW_LINE
                + TAP
                + "Query Count in Transaction: " + count
                + NEW_LINE
                + NEW_LINE
                + category
                + NEW_LINE
                + NEW_LINE
                + "====================================================================================================";
    }

    private String formatBySql(String sql, String category) {
        if (isStatementDDL(sql, category)) {
            return NEW_LINE
                    + "Execute DDL (Query Count in Transaction: " + queryCount + "): "
                    + NEW_LINE
                    + FormatStyle.DDL
                    .getFormatter()
                    .format(sql);
        }
        return NEW_LINE
                + "Execute DML (Query Count in Transaction: " + queryCount + "): "
                + NEW_LINE
                + FormatStyle.BASIC
                .getFormatter()
                .format(sql);
    }

    private String getAdditionalMessages(long elapsed) {
        return NEW_LINE
                + NEW_LINE
                + String.format("Execution Time: %s ms", elapsed)
                + NEW_LINE
                + "----------------------------------------------------------------------------------------------------";
    }

    private boolean isStatementDDL(String sql, String category) {
        return isStatement(category) && isDDL(sql.trim().toLowerCase(Locale.ROOT));
    }

    private boolean isStatement(String category) {
        return Category.STATEMENT.getName().equals(category);
    }

    private boolean isDDL(String lowerSql) {
        return lowerSql.startsWith(CREATE)
                || lowerSql.startsWith(ALTER)
                || lowerSql.startsWith(DROP)
                || lowerSql.startsWith(COMMENT);
    }
}
