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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModRulesets {

    public static final TSLPlatform TSL = new TSLPlatform("StreamSpawn", 1.0f);

    public static Map<String, TSLRuleset> RULESET_REGISTRY = new HashMap<>();

    public static void initialize() throws TSLSyntaxException, IOException {
        TSL.initializeStd();

        TSL.registerAction("PRINT", (tslPlatform, actionArgs)
                -> new TSLAction(tslPlatform, actionArgs) {
            @Override
            public boolean perform(TSLEventContext ctx) {
                System.out.println(actionArgs + "|" + ctx.getEventArgs());
                return true;
            }
        });

        String dummyTarget = "Minecraft:DummyPlayer";
        String dummyRuleset = """
                IF dt = 1000 THEN 
                 PRINT %1s passed%
                ELSE PRINT %Yooo, not 1s passed%
                ON Clock Tick
                WITH actor IS %Clock%
                """;

        TSLLexer lexer = new TSLLexer(CharStream.fromString(dummyRuleset));
        TSLParser parser = new TSLParser(TSL, dummyTarget, lexer.tokenize());
        TSLRuleset ruleset = parser.parse();

        RULESET_REGISTRY.put(dummyTarget, ruleset);
    }

    public static void triggerEvent(TSLEventContext ctx) {
        try {
            RULESET_REGISTRY.get(ctx.getTarget()).perform(ctx);

        } catch (TSLPerformingException e) {
            throw new RuntimeException(e);
        }
    }

}
