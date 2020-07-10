package cc.sfclub.mirai.utils;

import cc.sfclub.core.Core;
import cc.sfclub.mirai.misc.UIDMap;
import cc.sfclub.mirai.packets.received.message.MiraiMessage;
import cc.sfclub.mirai.packets.received.message.MiraiTypeMessage;
import cc.sfclub.mirai.packets.received.message.types.At;
import cc.sfclub.mirai.packets.received.message.types.Image;
import cc.sfclub.mirai.packets.received.message.types.Plain;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MessageUtil {
    public static boolean isMiraiEvent(String type) {
        if (type.endsWith("Event")) return true;
        if (type.startsWith("BotOfflineEvent")) return true;
        return false;
    }

    public static String deserializeChain(List<MiraiTypeMessage> chain) {
        StringBuilder cctext = new StringBuilder();
        chain.forEach(c -> {
            cctext.append(deserialzeTypeMessage(c));
        });
        return cctext.toString();
    }

    public static String deserialzeTypeMessage(MiraiTypeMessage message) {
        StringBuilder builder = new StringBuilder();
        switch (message.getType()) {
            case "Source":
                return "";//We don't need it in catcodes.
            case "At":
                builder.append("[At:").append(UIDMap.fromQQUIN(((At) message).getTarget()).orElseThrow(NullPointerException::new).getQQUIN()).append(']');
                return builder.toString();
            case "AtAll":
                return "[AtAll]";
            case "Image":
                Image image = (Image) message;
                builder.append("[Image:").append(Base64.getUrlEncoder().encodeToString(image.getUrl().getBytes())).append(']');
                return builder.toString();
            case "Plain":
                return ((Plain) message).getText();
        }
        if (Core.get().config().isDebug()) {
            Core.getLogger().warn("[MiraiAdapter] Unsupported message: {}", message.getType());
        }
        return "";
    }

    public static MessageChainBuilder buildChain() {
        return new MessageChainBuilder();
    }

    public static class MessageChainBuilder {
        private List<MiraiMessage> messageChain = new ArrayList<>();

        public List<MiraiMessage> build() {
            return messageChain;
        }

        public MessageChainBuilder append(MiraiMessage m) {
            messageChain.add(m);
            return this;
        }
    }
}
