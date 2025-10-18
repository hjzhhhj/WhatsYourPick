package com.whatsyourpick.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class StartPanel extends JPanel {

    private RoundedButton startButton;
    private BufferedImage backgroundImage;
    private static final Color PINK_COLOR = new Color(241, 113, 151); // #F17197

    public StartPanel() {
        setLayout(new GridBagLayout());
        loadBackgroundImage();
        initComponents();
    }

    private void loadBackgroundImage() {
        try {            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/images/start-background.png"));
        } catch (Exception e) {
            // 배경 이미지가 없으면 null 처리
            backgroundImage = null;
            System.out.println("배경 이미지를 찾을 수 없습니다. 단색 배경을 사용합니다.");
            if (e != null) {
                System.err.println("상세 오류: " + e.getMessage());
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(255, 243, 253)); // #FFF3FD
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 20, 0);

        // Pick Me 타이틀
        JLabel titleLabel = new JLabel("Pick Me");
        titleLabel.setFont(FontManager.getPressStart2P(Font.BOLD, 64f));
        titleLabel.setForeground(PINK_COLOR); // #F17197
        gbc.insets = new Insets(80, 0, 20, 0);
        gbc.gridy = 0;
        add(titleLabel, gbc);

        startButton = new RoundedButton("START \u2192");
        startButton.setFont(FontManager.getPressStart2P(20f));
        startButton.setPreferredSize(new Dimension(250, 70));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.insets = new Insets(40, 0, 20, 0);
        gbc.gridy = 1;
        add(startButton, gbc);
    }

    /**
     * START 버튼에 액션 리스너를 추가합니다.
     * @param listener 액션 리스너
     */
    public void addStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    /**
     * 둥근 모서리 버튼 클래스
     */
    private static class RoundedButton extends JButton {
        private static final Color BUTTON_BG = new Color(255, 243, 253); // #FFF3FD
        private static final Color BORDER_COLOR = new Color(241, 113, 151); // #F17197
        private static final Color HOVER_COLOR = new Color(255, 230, 245); // 호버 시 약간 어두운 색
        private static final int BORDER_WIDTH = 5;
        private static final int ARC_WIDTH = 35; // 둥글기 정도
        private boolean isHovered = false;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(BORDER_COLOR);

            // 호버 효과
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    isHovered = true;
                    repaint();
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // 배경 색상 (호버 시 변경)
            if (isHovered) {
                g2.setColor(HOVER_COLOR);
            } else {
                g2.setColor(BUTTON_BG);
            }
            g2.fillRoundRect(0, 0, width - 1, height - 1, ARC_WIDTH, ARC_WIDTH);

            // 테두리 (5px)
            g2.setColor(BORDER_COLOR);
            g2.setStroke(new BasicStroke(BORDER_WIDTH));
            g2.drawRoundRect(BORDER_WIDTH / 2, BORDER_WIDTH / 2,
                           width - BORDER_WIDTH, height - BORDER_WIDTH,
                           ARC_WIDTH, ARC_WIDTH);

            g2.dispose();

            // 텍스트 그리기
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            // 테두리는 paintComponent에서 그리므로 여기서는 아무것도 하지 않음
        }
    }
}