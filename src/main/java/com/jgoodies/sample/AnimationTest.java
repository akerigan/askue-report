package com.jgoodies.sample;

import com.jgoodies.animation.*;
import com.jgoodies.animation.animations.BasicTextAnimation;
import com.jgoodies.animation.animations.BasicTextAnimations;
import com.jgoodies.animation.components.BasicTextLabel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.WHITE;


/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 29.09.2010 22:40:53 (Europe/Moscow)
 */
public class AnimationTest {

// require(url:"jgoodies.com", jar:"animation", version:"1.2.0")
// require(url:"jgoodies.com", jar:"forms", version:"1.1.0")

// Based on the jgoodies animation tutorial class:
//     com.jgoodies.animation.tutorial.intro.BasicTextLabelIntro

    static class AnimateAction extends AbstractAction {

        Animation animation;

        AnimateAction(Animation animation) {
            this.animation = animation;
        }

        public void actionPerformed(java.awt.event.ActionEvent e) {
            animation.addAnimationListener(new AnimationListener() {
                public void animationStarted(AnimationEvent evt) {
                    enabled = false;
                }

                public void animationStopped(AnimationEvent evt) {
                    enabled = false;
                }
            });
            new Animator(animation, 30 /*fps*/).start();
        }
    }

    private static JPanel buildPanel(BasicTextLabel[] labels) {
        FormLayout layout = new FormLayout("fill:pref:grow", "fill:pref:grow, p, p");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.add(buildPreviewPanel(labels), cc.xy(1, 1));
        builder.addSeparator("", cc.xy(1, 2));
        builder.add(buildToolsPanel(labels), cc.xy(1, 3));
        return builder.getPanel();
    }

    private static JPanel buildPreviewPanel(BasicTextLabel[] labels) {
        FormLayout layout = new FormLayout("fill:200dlu:grow", "fill:100dlu:grow");
        JPanel panel = new JPanel(layout);
        CellConstraints cc = new CellConstraints();
        panel.setBackground(WHITE);
        panel.add(labels[0], cc.xy(1, 1));
        panel.add(labels[1], cc.xy(1, 1));
        return panel;
    }

    private static JPanel buildToolsPanel(BasicTextLabel[] labels) {
        FormLayout layout = new FormLayout("right:pref:grow", "pref");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        AnimateAction action = new AnimateAction(createAnimation(labels));
        action.putValue("Name", "Animate");
        builder.add(new JButton(action), cc.xy(1, 1));
        return builder.getPanel();
    }

    private static Animation createAnimation(BasicTextLabel[] labels) {
        Animation[] animations = new Animation[]{
                Animations.pause(1000),
                BasicTextAnimation.defaultFade(labels[0], 2500, "Welcome To", DARK_GRAY),
                Animations.pause(1000),
                BasicTextAnimation.defaultFade(labels[0], 3000, "JGoodies Animation", DARK_GRAY),
                Animations.pause(1000),
                BasicTextAnimations.defaultFade(labels[0], labels[1], 2000, -100,
                        "An open source framework|for time-based|" +
                                "real-time animations|in Java and Groovy.",
                        DARK_GRAY),
                Animations.pause(1000),
                BasicTextAnimations.defaultFade(labels[0], labels[1], 3000, 500, "Main Features:", DARK_GRAY),
                Animations.pause(1000),
                BasicTextAnimations.defaultFade(labels[0], labels[1], 1750, 0,
                        "Seamless|flexible|and powerful integration|" +
                                "with Java and Groovy.|Small library size.",
                        DARK_GRAY),
                Animations.pause(1500)
        };
        return Animations.sequential(animations);
    }

    private static BasicTextLabel buildLabel(Font font) {
        BasicTextLabel label = new BasicTextLabel(" ");
        label.setFont(font);
        label.setOpaque(false);
        return label;
    }

    private static void centerOnScreen(JFrame component) {
        Dimension paneSize = component.getSize();
        Dimension screenSize = component.getToolkit().getScreenSize();
        int x = (int) ((screenSize.getWidth() - paneSize.getWidth()) / 2);
        int y = (int) ((screenSize.getHeight() - paneSize.getHeight()) * 0.45);
        component.setLocation(x, y);
    }

    public static void main(String[] args) {
        Font font = new Font("Tahoma", Font.BOLD, 18);
        BasicTextLabel label1 = buildLabel(font);
        BasicTextLabel label2 = buildLabel(font);
        JFrame frame = new JFrame();
        frame.setTitle("Groovy/JGoodies Animation Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = buildPanel(new BasicTextLabel[]{label1, label2});
        frame.getContentPane().add(panel);
        frame.pack();
        centerOnScreen(frame);
        frame.setVisible(true);
    }

}
