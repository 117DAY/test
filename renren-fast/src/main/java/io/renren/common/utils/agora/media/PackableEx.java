package io.renren.common.utils.agora.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
