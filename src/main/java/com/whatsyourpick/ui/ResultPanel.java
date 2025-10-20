package com.whatsyourpick.ui;

import com.whatsyourpick.model.Contestant;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 결과 화면 패널
 * 우승자 정보와 재시작 버튼을 표시합니다.
 */
public class ResultPanel extends JPanel {

    private JLabel winnerImageLabel;
    private JLabel winnerNameLabel;
    private JLabel resultTextLabel;
    private JButton anotherGamesButton;
    private JButton restartButton;
    private String categoryName;

    public ResultPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        initComponents();
    }

    private void initComponents() {
        // 전체 컨테이너
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 40, 30, 40);

        // 좌측: 우승자 표시
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(245, 245, 250));

        // 우승자 이미지
        winnerImageLabel = new JLabel();
        winnerImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winnerImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        winnerImageLabel.setPreferredSize(new Dimension(400, 400));
        winnerImageLabel.setMaximumSize(new Dimension(400, 400));
        winnerImageLabel.setBackground(new Color(240, 240, 245));
        winnerImageLabel.setOpaque(true);
        winnerImageLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 5)); // 금색 테두리
        winnerImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 우승자 이름
        winnerNameLabel = new JLabel("");
        winnerNameLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 28f));
        winnerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        winnerNameLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        leftPanel.add(winnerImageLabel);
        leftPanel.add(winnerNameLabel);

        gbc.gridx = 0;
        mainPanel.add(leftPanel, gbc);

        // 우측: 결과 정보 및 버튼
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(245, 245, 250));

        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.gridx = 0;
        rightGbc.insets = new Insets(15, 0, 15, 0);

        // 결과 텍스트
        resultTextLabel = new JLabel("");
        resultTextLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 24f));
        resultTextLabel.setForeground(new Color(75, 0, 130));
        rightGbc.gridy = 0;
        rightGbc.insets = new Insets(0, 0, 50, 0);
        rightPanel.add(resultTextLabel, gbc);

        // ANOTHER GAMES 버튼
        anotherGamesButton = new JButton("ANOTHER GAMES \u2192");
        anotherGamesButton.setFont(FontManager.getPressStart2P(16f));
        anotherGamesButton.setPreferredSize(new Dimension(380, 65));
        anotherGamesButton.setBackground(new Color(75, 0, 130));
        anotherGamesButton.setForeground(Color.WHITE);
        anotherGamesButton.setFocusPainted(false);
        anotherGamesButton.setBorderPainted(false);
        anotherGamesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        anotherGamesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                anotherGamesButton.setBackground(new Color(100, 20, 160));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                anotherGamesButton.setBackground(new Color(75, 0, 130));
            }
        });

        rightGbc.gridy = 1;
        rightGbc.insets = new Insets(15, 0, 15, 0);
        rightPanel.add(anotherGamesButton, rightGbc);

        // RESTART 버튼
        restartButton = new JButton("RESTART \u2192");
        restartButton.setFont(FontManager.getPressStart2P(16f));
        restartButton.setPreferredSize(new Dimension(380, 65));
        restartButton.setBackground(new Color(220, 20, 60)); // 빨간색
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusPainted(false);
        restartButton.setBorderPainted(false);
        restartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        restartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                restartButton.setBackground(new Color(250, 50, 90));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                restartButton.setBackground(new Color(220, 20, 60));
            }
        });

        rightGbc.gridy = 2;
        rightPanel.add(restartButton, rightGbc);

        gbc.gridx = 1;
        mainPanel.add(rightPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * 우승자 정보를 설정합니다.
     */
    public void setWinner(Contestant winner, String categoryName) {
        this.categoryName = categoryName;

        // 우승자 이름 설정
        winnerNameLabel.setText(winner.getName());

        // 결과 텍스트 설정 (요구사항: "1th ~~의 ~~~")
        resultTextLabel.setText("<html><center>1st<br>" + categoryName + "의<br>" + winner.getName() + "</center></html>");

        // 이미지 로드
        loadImage(winner.getImagePath());
    }

    /**
     * 이미지를 로드합니다.
     */
// ResultPanel.java 파일
// ...
    /**
     * 이미지를 로드합니다.
     */
    private void loadImage(String imagePath) {
        try {
            // 1. DB 경로의 맨 앞 '/'를 제거하여 'images/...' 형태로 만듦
            // (imagePath가 null이거나 비어있지 않은 경우에만 처리)
            String cleanPath = (imagePath != null && imagePath.startsWith("/"))
                    ? imagePath.substring(1)
                    : imagePath;

            // 2. 클래스패스 기준으로 로드하기 위해 다시 맨 앞에 '/'를 붙여 URL로 가져옴
            // (DB 경로에 슬래시가 하나만 붙도록 보장)
            java.net.URL imageUrl = getClass().getResource("/" + cleanPath);

            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);

                if (icon.getIconWidth() > 0) {
                    Image scaledImage = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
                    winnerImageLabel.setIcon(new ImageIcon(scaledImage));
                    winnerImageLabel.setText("");
                } else {
                    winnerImageLabel.setIcon(null);
                    winnerImageLabel.setText("이미지 없음");
                    winnerImageLabel.setFont(FontManager.getDungGeunMo(16f));
                }
            } else {
                // URL이 null인 경우
                winnerImageLabel.setIcon(null);
                winnerImageLabel.setText("이미지 경로 오류");
                winnerImageLabel.setFont(FontManager.getDungGeunMo(16f));
            }
        } catch (Exception e) {
            winnerImageLabel.setIcon(null);
            winnerImageLabel.setText("이미지 로드 오류");
            winnerImageLabel.setFont(FontManager.getDungGeunMo(16f));
        }
    }
// ...

    /**
     * ANOTHER GAMES 버튼에 액션 리스너를 추가합니다.
     */
    public void addAnotherGamesListener(ActionListener listener) {
        anotherGamesButton.addActionListener(listener);
    }

    /**
     * RESTART 버튼에 액션 리스너를 추가합니다.
     */
    public void addRestartListener(ActionListener listener) {
        restartButton.addActionListener(listener);
    }
}