/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

public class WrapperGameClientResourcePackStatus extends PacketWrapper<WrapperGameClientResourcePackStatus> {
    private Optional<String> hash;
    private Result result;

    public WrapperGameClientResourcePackStatus(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperGameClientResourcePackStatus(Result result) {
        super(PacketType.Game.Client.RESOURCE_PACK_STATUS.getID());
        this.result = result;
    }

    @Deprecated
    public WrapperGameClientResourcePackStatus(String hash, Result result) {
        super(PacketType.Game.Client.RESOURCE_PACK_STATUS.getID());
        this.hash = Optional.of(hash);
        this.result = result;
    }

    @Override
    public void readData() {
        if (clientVersion.isOlderThan(ClientVersion.v_1_10)) {
            //For now ignore hash, maybe make optional
            this.hash = Optional.of(readString(40));
        }
        int resultIndex = readVarInt();
        this.result = Result.VALUES[resultIndex];
    }

    @Override
    public void readData(WrapperGameClientResourcePackStatus wrapper) {
        this.hash = wrapper.hash;
        this.result = wrapper.result;
    }

    @Override
    public void writeData() {
        if (clientVersion.isOlderThan(ClientVersion.v_1_10)) {
            writeString(hash.orElse("invalid"), 40);
        }
        writeVarInt(result.ordinal());
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Optional<String> getHash() {
        return hash;
    }

    public void setHash(Optional<String> hash) {
        this.hash = hash;
    }

    public enum Result {
        SUCCESSFULLY_LOADED,
        DECLINED,
        FAILED_DOWNLOAD,
        ACCEPTED;

        public static final Result[] VALUES = values();
    }
}