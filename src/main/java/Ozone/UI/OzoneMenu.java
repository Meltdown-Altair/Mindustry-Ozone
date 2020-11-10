package Ozone.UI;


import Atom.Reflect.Reflect;
import Ozone.Commands.Commands;
import Ozone.Interface;
import Ozone.Manifest;
import Ozone.Patch.ImprovisedKeybinding;
import Settings.Core;
import arc.input.KeyCode;
import arc.scene.ui.TextField;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

public class OzoneMenu extends BaseDialog {
    private TextField commandsField;
    private String commands = "";

    public OzoneMenu(String title, DialogStyle style) {
        super(title, style);
        this.keyDown((key) -> {
            if (key == KeyCode.escape || key == KeyCode.back) {
                arc.Core.app.post(this::hide);
            }else if (key == KeyCode.enter) {
                Commands.call(Core.commandsPrefix + commands);
                commands = "";
                commandsField.clearText();
            }
        });
        this.shown(this::setup);
        this.onResize(this::setup);
        Interface.registerKeybinding(new ImprovisedKeybinding("ozone.menu", KeyCode.backtick, "Ozone", ImprovisedKeybinding.mode.tap), this::show);
    }


    @Override
    public void hide() {
        super.hide();
        try {
            if (!Vars.ui.hudfrag.shown)
                Reflect.getMethod(null, "toggleMenus", Vars.ui.hudfrag).invoke(Vars.ui.hudfrag);
        }catch (Throwable ignored) {
        }
    }

    public void setup() {
        cont.top();
        cont.clear();
        //   cont.button(Ozone.Core.bundle.get("ozone.javaEditor"), Icon.pencil, () -> {
        //      Ozone.Core.app.post(this::hide);
        //     Manifest.commFrag.toggle();
        // }).size(Ozone.Core.graphics.getWidth() / 6, Ozone.Core.graphics.getHeight() / 12);
        cont.row();
        cont.button(arc.Core.bundle.get("ozone.commandsUI"), Icon.commandRally, () -> {
            arc.Core.app.post(this::hide);
            Manifest.commFrag.toggle();
        }).size(arc.Core.graphics.getWidth() / 6, arc.Core.graphics.getHeight() / 12);
        cont.row();
        cont.table((s) -> {
            s.left();
            s.label(() -> arc.Core.bundle.get("Commands") + ": ");
            commandsField = s.field(commands, (res) -> commands = res).fillX().growX().get();
            s.button(Icon.zoom, () -> {
                Commands.call(Core.commandsPrefix + commands);
                //Commands.call(commands);
                commands = "";
                commandsField.clearText();
            });
        }).growX().fillX().padBottom(6.0F).bottom().size(arc.Core.graphics.getWidth(), arc.Core.graphics.getHeight() / 12);


        try {
            if (Vars.ui.hudfrag.shown)
                Reflect.getMethod(null, "toggleMenus", Vars.ui.hudfrag).invoke(Vars.ui.hudfrag);
        }catch (Throwable ignored) {
        }
    }
}
