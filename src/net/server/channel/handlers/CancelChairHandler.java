/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation version 3 as published by
 the Free Software Foundation. You may not use, modify or distribute
 this program under any other version of the GNU Affero General Public
 License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.server.channel.handlers;

import client.MapleClient;
import client.MapleCharacter;
import net.AbstractMaplePacketHandler;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public final class CancelChairHandler extends AbstractMaplePacketHandler {
    
    @Override
    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int id = slea.readShort();
        MapleCharacter mc = c.getPlayer();
        
        if (id == -1) { // Cancel Chair
            mc.setChair(0);
            if(mc.unregisterChairBuff()) {
                c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.cancelForeignChairSkillEffect(mc.getId()), false);
            }
            
            c.announce(MaplePacketCreator.cancelChair(-1));
            c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.showChair(c.getPlayer().getId(), 0), false);
        } else { // Use In-Map Chair
            mc.setChair(id);
            if(mc.registerChairBuff()) {
                c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.giveForeignChairSkillEffect(mc.getId()), false);
            }
            
            c.announce(MaplePacketCreator.cancelChair(id));
        }
    }
}
