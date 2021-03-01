package com.uncles.novel.app.jfx.framework.ui.components;

import com.jfoenix.controls.JFXButton;
import com.uncles.novel.app.jfx.framework.ui.components.icon.Icon;
import com.uncles.novel.app.jfx.framework.ui.components.icon.SvgIcon;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;

/**
 * @author blog.unclezs.com
 * @date 2021/02/28 1:28
 */
public class IconButton extends JFXButton {
    public static final String DEFAULT_STYLE_CLASS = "icon-button";
    private String icon;
    private String svg;
    private String tip;

    public IconButton() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    public IconButton(String text) {
        this(text, null, null);
    }

    public IconButton(String text, String tip) {
        this(text, null, tip);
    }

    public IconButton(Character icon, String tip) {
        this(null, icon, tip);
    }

    public IconButton(String text, Character icon, String tip) {
        super(text);
        setIcon(icon);
        setTip(tip);
        setCursor(Cursor.HAND);
    }

    public String getIcon() {
        return icon;
    }

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
        if (svg != null) {
            this.setGraphic(new SvgIcon(svg));
        }
    }

    public void setIcon(String icon) {
        this.icon = icon;
        if (icon != null) {
            this.setGraphic(new Icon(icon));
        }
    }

    public void setIcon(Icon icon) {
        if (icon != null) {
            this.setGraphic(icon);
        }
    }

    public void setIcon(Character icon) {
        if (icon != null) {
            this.setGraphic(new Icon(icon));
        }
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
        if (tip != null) {
            this.setTooltip(new Tooltip(tip));
        }
    }
}