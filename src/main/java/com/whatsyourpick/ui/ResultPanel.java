package com.whatsyourpick.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import com.whatsyourpick.model.Contestant;

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
    private Runnable backButtonListener; // 헤더 클릭 시 돌아가기 위한 리스너 추가

    // 배경 및 색상 변수 추가 (다른 패널과 동일)
    private BufferedImage backgroundImage;
    private static final Color PINK_COLOR = new Color(241, 113, 151); // #F17197
    private static final Color HEADER_BG_COLOR = new Color(255, 209, 233); // #FFD1E9

    public ResultPanel() {
        // 배경 이미지 로드 로직 추가
        loadBackgroundImage();
        setLayout(new BorderLayout());
        // setBackground(new Color(245, 245, 250)); // 배경은 paintComponent에서 처리
        initComponents();
    }

    // 배경 이미지 로드 메서드 추가
    private void loadBackgroundImage() {
        try {
            java.net.URL imageUrl = getClass().getResource("/images/background.png");

            if (imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
            } else {
                backgroundImage = null;
                System.err.println("오류: 배경 이미지를 찾을 수 없습니다. 경로: /images/background.png");
            }
        } catch (Exception e) {
            backgroundImage = null;
            System.err.println("배경 이미지 로드 중 상세 오류: " + e.getMessage());
        }
    }

    // paintComponent 메서드 추가 (배경 이미지 그리기)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(255, 243, 253)); // #FFF3FD (배경 이미지 없을 시 대체 색상)
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void initComponents() {
        // 1. 상단 헤더 패널 (♥️ Pick Me) - 다른 패널과 동일하게 추가
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        headerPanel.setBackground(HEADER_BG_COLOR);
        headerPanel.setBorder(new MatteBorder(0, 0, 3, 0, PINK_COLOR));
        headerPanel.setOpaque(true);
        headerPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel headerLabel = new JLabel("\u2665\ufe0f  Pick Me");
        headerLabel.setFont(FontManager.getPressStart2P(Font.BOLD, 32f));
        headerLabel.setForeground(PINK_COLOR);
        headerPanel.add(headerLabel);

// 헤더 클릭 이벤트
        headerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (backButtonListener != null) {
                    backButtonListener.run();
                }
            }
        });

        add(headerPanel, BorderLayout.NORTH);

        // 2. 전체 컨테이너
        JPanel mainPanel = new JPanel(new GridBagLayout());
        // 배경 이미지 위에 올라가므로 투명하게 설정
        mainPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 40, 30, 40);

        // 좌측: 우승자 표시
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false); // 투명 설정

        // 우승자 이미지
        winnerImageLabel = new RoundedLabel();
        winnerImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winnerImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        winnerImageLabel.setPreferredSize(new Dimension(500, 480));
        winnerImageLabel.setMaximumSize(new Dimension(500, 480));
        winnerImageLabel.setBackground(new Color(240, 240, 245));
        winnerImageLabel.setOpaque(false); // IMPORTANT: 직접 그리기 때문에 false
        winnerImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 라운드 설정
        ((RoundedLabel) winnerImageLabel).setCornerRadius(40);
        ((RoundedLabel) winnerImageLabel).setBorderThickness(0);

        winnerImageLabel.setText("우승자 이미지");

        // 우승자 이름
        winnerNameLabel = new JLabel("");
        winnerNameLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 28f));
        winnerNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        winnerNameLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        winnerNameLabel.setOpaque(false); // 투명 설정

        leftPanel.add(winnerImageLabel);

        gbc.gridx = 0;
        mainPanel.add(leftPanel, gbc);

        // 우측: 결과 정보 및 버튼
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false); // 투명 설정

        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.gridx = 0;
        rightGbc.insets = new Insets(15, 0, 15, 0);

        // 결과 텍스트
        resultTextLabel = new JLabel("");
        resultTextLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 24f));
        resultTextLabel.setForeground(new Color(241, 113, 151));
        rightGbc.gridy = 0;
        rightGbc.insets = new Insets(0, 0, 50, 0);
        rightPanel.add(resultTextLabel, rightGbc); // gbc -> rightGbc 수정

        // ANOTHER GAMES 버튼
        anotherGamesButton = new RoundedButton("ANOTHER GAMES \u2192", 60);
        anotherGamesButton.setFont(FontManager.getPressStart2P(16f));
        anotherGamesButton.setPreferredSize(new Dimension(380, 65));
        anotherGamesButton.setBackground(new Color(241, 113, 151));
        anotherGamesButton.setForeground(Color.WHITE);
        anotherGamesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        anotherGamesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                anotherGamesButton.setBackground(new Color(241, 113, 151));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                anotherGamesButton.setBackground(new Color(241, 113, 151));
            }
        });

        rightGbc.gridy = 1;
        rightGbc.insets = new Insets(15, 0, 15, 0);
        rightPanel.add(anotherGamesButton, rightGbc);

        // RESTART 버튼
        restartButton = new RoundedButton("RESTART \u2192", 60);
        restartButton.setFont(FontManager.getPressStart2P(16f));
        restartButton.setPreferredSize(new Dimension(380, 65));
        restartButton.setBackground(new Color(241, 113, 151));
        restartButton.setForeground(Color.WHITE);
        restartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover 이벤트
        restartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                restartButton.setBackground(new Color(241, 113, 151)); // 밝게
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                restartButton.setBackground(new Color(241, 113, 151)); // 원래색
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
        resultTextLabel.setText("<html><center>1등<br>" + categoryName + "의<br>" + winner.getName() + "</center></html>");

        // 이미지 로드
        loadImage(winner.getImagePath());
    }

    /**
     * 이미지를 로드하고 500x480으로 크롭합니다.
     */
    private void loadImage(String imagePath) {
        try {
            // 경로 정리 (맨 앞의 / 제거)
            String cleanPath = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;

            System.out.println("🖼️  이미지 로드 시도: " + cleanPath);

            // 리소스에서 이미지 로드 시도
            java.net.URL imageUrl = getClass().getClassLoader().getResource(cleanPath);

            if (imageUrl != null) {
                BufferedImage originalImg = ImageIO.read(imageUrl);

                if (originalImg != null && originalImg.getWidth() > 0 && originalImg.getHeight() > 0) {
                    // 타겟 크기
                    int targetWidth = 500;
                    int targetHeight = 480;

                    // 원본 이미지의 비율 계산
                    double imgRatio = (double) originalImg.getWidth() / originalImg.getHeight();
                    double targetRatio = (double) targetWidth / targetHeight;

                    int cropWidth, cropHeight;

                    // 이미지를 크롭할 크기 결정 (중앙에서 잘라내기)
                    if (imgRatio > targetRatio) {
                        // 이미지가 더 넓음 - 높이를 기준으로 폭을 자름
                        cropHeight = originalImg.getHeight();
                        cropWidth = (int) (cropHeight * targetRatio);
                    } else {
                        // 이미지가 더 높음 - 폭을 기준으로 높이를 자름
                        cropWidth = originalImg.getWidth();
                        cropHeight = (int) (cropWidth / targetRatio);
                    }

                    // 중앙에서 크롭
                    int x = (originalImg.getWidth() - cropWidth) / 2;
                    int y = (originalImg.getHeight() - cropHeight) / 2;

                    BufferedImage croppedImg = originalImg.getSubimage(x, y, cropWidth, cropHeight);

                    // 타겟 크기로 스케일링
                    Image scaledImage = croppedImg.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

                    winnerImageLabel.setIcon(new ImageIcon(scaledImage));
                    winnerImageLabel.setText("");
                    System.out.println("✅ 이미지 로드 성공");
                } else {
                    setImageNotFound("이미지 크기 0");
                }
            } else {
                setImageNotFound("리소스를 찾을 수 없음: " + cleanPath);
            }

        } catch (Exception e) {
            setImageNotFound(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 이미지를 찾을 수 없을 때 표시
     */
    private void setImageNotFound(String reason) {
        winnerImageLabel.setIcon(null);
        winnerImageLabel.setText("<html><center>이미지 없음</center></html>");
        winnerImageLabel.setFont(FontManager.getDungGeunMo(14f));
        winnerImageLabel.setForeground(new Color(150, 150, 150));
        System.err.println("❌ 이미지 로드 실패: " + reason);
    }

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

    /**
     * 헤더(♥️ Pick Me) 클릭 리스너를 설정합니다.
     * @param listener 헤더 클릭 리스너
     */
    public void setBackButtonListener(Runnable listener) {
        this.backButtonListener = listener;
    }

    // ---------------------------------------------------
// 둥근 모서리 버튼 클래스
// ---------------------------------------------------
    public class RoundedButton extends JButton {

        private int radius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setOpaque(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // 배경색으로 둥근 사각형 칠하기
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            // 텍스트/아이콘 그리기
            super.paintComponent(g2);

            g2.dispose();
        }
    }

    public class RoundedLabel extends JLabel {

        private int cornerRadius = 30;   // 둥근 정도
        private Color borderColor = new Color(255, 215, 0); // 금색
        private int borderThickness = 5;

        public RoundedLabel() {
            super();
            setOpaque(false); // 직접 배경을 그릴 때는 false 유지
        }

        public void setCornerRadius(int radius) {
            this.cornerRadius = radius;
            repaint();
        }

        public void setBorderColor(Color color) {
            this.borderColor = color;
            repaint();
        }

        public void setBorderThickness(int thickness) {
            this.borderThickness = thickness;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 아이콘이 있을 경우 둥근 모서리로 클리핑
            if (getIcon() != null && getIcon() instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) getIcon();
                Image img = icon.getImage();

                // 둥근 사각형으로 클리핑
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
                g2.drawImage(img, 0, 0, getWidth(), getHeight(), this);

                // 테두리
                if (borderThickness > 0) {
                    g2.setClip(null); // 클립 제거
                    g2.setColor(borderColor);
                    g2.setStroke(new BasicStroke(borderThickness));
                    g2.drawRoundRect(borderThickness / 2, borderThickness / 2,
                            getWidth() - borderThickness, getHeight() - borderThickness,
                            cornerRadius, cornerRadius);
                }
            } else {
                // 아이콘이 없을 경우 기본 텍스트 표시
                super.paintComponent(g);
            }

            g2.dispose();
        }
    }
}

// test123