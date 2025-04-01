package net.programmer.igoodie.streamspawn.init;

import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.exception.TSLPerformingException;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.parser.CharStream;
import net.programmer.igoodie.tsl.parser.TSLLexer;
import net.programmer.igoodie.tsl.parser.TSLParser;
import net.programmer.igoodie.tsl.runtime.TSLRuleset;
import net.programmer.igoodie.tsl.runtime.action.TSLAction;
import net.programmer.igoodie.tsl.runtime.event.TSLEventContext;
import net.programmer.igoodie.tsl.util.LogFormatter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class ModTSL {

    public static final TSLPlatform TSL = new TSLPlatform("StreamSpawn", 1.0f);

    public static Map<String, TSLRuleset> RULESET_REGISTRY = new HashMap<>();

    public static void initializePlatform() {
        TSL.initializeStd();
        initTwitchSpawnCompat();

        TSL.registerAction("PRINT", (tslPlatform, actionArgs)
                -> new TSLAction(tslPlatform, actionArgs) {
            @Override
            public boolean perform(TSLEventContext ctx) {
                System.out.println(actionArgs + "|" + ctx.getEventArgs());
                return true;
            }
        });
    }

    public static void loadRulesets() {
        String dummyTarget = "Minecraft:DummyPlayer";
        String dummyRuleset = """
                IF dt = 1000 THEN
                 PRINT %1s passed%
                ELSE PRINT %Yooo, not 1s passed%
                ON Clock Tick
                #WITH actor IS %Clock%
                """;

        try {
            TSLLexer lexer = new TSLLexer(CharStream.fromString(dummyRuleset));
            TSLParser parser = new TSLParser(TSL, dummyTarget, lexer.tokenize());
            TSLRuleset ruleset = parser.parse();
            RULESET_REGISTRY.put(dummyTarget, ruleset);
        } catch (IOException e) {
            // TODO: Handle properly
            System.err.println("Could not read input stream");
        } catch (TSLSyntaxException e) {
            // TODO: Handle properly
            System.err.println("TSL Syntax error @ " + e.getMessage());
        }
    }

    protected static DateFormat getDateFormat(String pattern, TimeZone timezone) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(timezone);
        return dateFormat;
    }

    protected static void initTwitchSpawnCompat() {
        final DateFormat DATE_FORMAT = getDateFormat("dd-MM-yyyy", TimeZone.getDefault());
        final DateFormat TIME_FORMAT = getDateFormat("HH:mm:ss", TimeZone.getDefault());
        final DateFormat UTC_DATE_FORMAT = getDateFormat("dd-MM-yyyy", TimeZone.getTimeZone("UTC"));
        final DateFormat UTC_TIME_FORMAT = getDateFormat("HH:mm:ss", TimeZone.getTimeZone("UTC"));

        TSL.registerExpression("event", (expr, ctx) -> Optional.of(ctx.getEventName()));
        TSL.registerExpression("streamer", (expr, ctx) -> Optional.of(ctx.getTarget()));
        TSL.registerExpression("actor", (expr, ctx) -> ctx.getEventArgs().getString(expr));
        TSL.registerExpression("message", (expr, ctx) -> ctx.getEventArgs().getString(expr).map(LogFormatter::escapeJson));
        TSL.registerExpression("message_unescaped", (expr, ctx) -> ctx.getEventArgs().getString("message"));
        TSL.registerExpression("title", (expr, ctx) -> ctx.getEventArgs().getString(expr));
        TSL.registerExpression("amount", (expr, ctx) -> ctx.getEventArgs().getDouble(expr).filter(num -> num != 0.0));
        TSL.registerExpression("amount_i", (expr, ctx) -> ctx.getEventArgs().getDouble("amount").filter(num -> num != 0.0).map(Double::intValue));
        TSL.registerExpression("amount_f", (expr, ctx) -> ctx.getEventArgs().getDouble("amount").filter(num -> num != 0.0).map(num -> String.format("%.2f", num)));
        TSL.registerExpression("currency", (expr, ctx) -> ctx.getEventArgs().getString(expr));
        TSL.registerExpression("months", (expr, ctx) -> ctx.getEventArgs().getInteger(expr).filter(num -> num != 0));
        TSL.registerExpression("tier", (expr, ctx) -> ctx.getEventArgs().getInteger(expr).filter(num -> num != -1).map(num -> num == 0 ? "Prime" : String.valueOf(num)));
        TSL.registerExpression("gifted", (expr, ctx) -> ctx.getEventArgs().getBoolean(expr));
        TSL.registerExpression("viewers", (expr, ctx) -> ctx.getEventArgs().getInteger(expr).filter(num -> num != 0));
        TSL.registerExpression("raiders", (expr, ctx) -> ctx.getEventArgs().getInteger(expr).filter(num -> num != 0));
        TSL.registerExpression("date", (expr, ctx) -> Optional.of(DATE_FORMAT.format(new Date())));
        TSL.registerExpression("date_utc", (expr, ctx) -> Optional.of(UTC_DATE_FORMAT.format(new Date())));
        TSL.registerExpression("time", (expr, ctx) -> Optional.of(TIME_FORMAT.format(new Date())));
        TSL.registerExpression("time_utc", (expr, ctx) -> Optional.of(UTC_TIME_FORMAT.format(new Date())));
        TSL.registerExpression("unix", (expr, ctx) -> Optional.of(Instant.now().getEpochSecond()));
    }

    public static void triggerEvent(TSLEventContext ctx) {
        try {
            RULESET_REGISTRY.get(ctx.getTarget()).perform(ctx);

        } catch (TSLPerformingException e) {
            throw new RuntimeException(e);
        }
    }

}
