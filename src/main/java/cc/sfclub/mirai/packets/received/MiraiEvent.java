package cc.sfclub.mirai.packets.received;

import lombok.Getter;

@Getter
public class MiraiEvent {
    private String type;
    private MiraiEvent event;
    public MiraiEvent() {
        type = this.getClass().getSimpleName();
    }
}
