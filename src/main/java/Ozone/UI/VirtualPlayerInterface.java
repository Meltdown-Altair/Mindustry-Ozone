/*
 * Copyright 2021 Itzbenz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Ozone.UI;

import Ozone.Bot.VirtualPlayer;
import arc.Core;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.gen.Icon;

public class VirtualPlayerInterface extends ScrollableDialog {
	VirtualPlayer virtualPlayer;
	
	public VirtualPlayerInterface(VirtualPlayer v) {
		super("Virtual Player " + v.vid());
		this.virtualPlayer = v;
	}
	
	@Override
	protected void setup() {
		table.table(t1 -> {
			t1.button("Connect", Icon.play, () -> {
				Vars.ui.showTextInput("Enter ip", "Ipee: ", Core.settings.getString("last-ip-connect", "localhost"), s -> {
					Vars.ui.showTextInput("Enter port", "port: ", Core.settings.getString("last-port-connect", Vars.port + ""), s1 -> {
						try {
							Core.settings.put("last-ip-connect", s);
							Core.settings.put("last-port-connect", s1);
							virtualPlayer.connect(s, Integer.parseInt(s1));
							init();
						}catch (Throwable t) {
							Vars.ui.showException(t);
						}
					});
				});
			}).growX().disabled(virtualPlayer.net.active());
			t1.button("Disconnect", Icon.exit, () -> {
				virtualPlayer.net.disconnect();
				init();
			}).growX().disabled(!virtualPlayer.net.active()).row();
			t1.button("Follow Me", () -> {
				virtualPlayer.followPlayer(Vars.player);
			}).growX().disabled(!virtualPlayer.state.equals(GameState.State.playing) || !Vars.net.active());
		}).growX();
		table.table(t2 -> {
			t2.labelWrap("Name: ").growX().row();
			t2.field(virtualPlayer.name, s -> virtualPlayer.name(s)).growX().row();
			t2.labelWrap("Status: " + virtualPlayer.state.toString()).growX().row();
			t2.labelWrap("ID: " + virtualPlayer.vid()).growX().row();
			t2.labelWrap("UUID: " + virtualPlayer.getUUID()).growX().row();
		}).growX();
		table.row();
		table.table(t2 -> t2.labelWrap(() -> virtualPlayer.log.toString()).growX().growY()).growX().growY();
	}
}
