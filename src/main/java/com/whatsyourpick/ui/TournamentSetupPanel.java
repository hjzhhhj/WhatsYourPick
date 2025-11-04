package com.whatsyourpick.ui;

import com.whatsyourpick.model.Category;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * 토너먼트 설정 화면 패널
 * 선택한 카테고리와 토너먼트 규모를 표시합니다.
 */
public class TournamentSetupPanel extends JPanel {

    private JLabel categoryImageLabel;
    private JLabel categoryNameLabel;
    private Runnable backButtonListener; // 헤더 클릭 시 돌아가기 위한 리스너
    private Map<Integer, RoundedButton> roundButtons;
    private RoundedButton startButton;
    private int selectedRound = 0;
    private Category selectedCategory;

    // 배경 및 색상 변수 추가
    private BufferedImage backgroundImage;
    private static final Color PINK_COLOR = new Color(241, 113, 151); // #F17197
    private static final Color HEADER_BG_COLOR = new Color(255, 209, 233); // #FFD1E9

    public TournamentSetupPanel() {
        // 배경 이미지 로드 로직 추가
        loadBackgroundImage();
        setLayout(new BorderLayout());
        roundButtons = new HashMap<>();
        initComponents();
    }

    // 배경 이미지 로드 메서드 추가
    private void loadBackgroundImage() {
        try {
            // getClass().getResourceAsStream() 대신 URL을 사용하여 좀 더 명확하게 처리
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

        // 1. 상단 헤더 패널 (♥️ Pick Me) - CategoryPanel과 동일하게 추가
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

        // 헤더 클릭 이벤트 (CategoryPanel과 동일하게 추가)
        headerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (backButtonListener != null) {
                    backButtonListener.run();
                }
            }
        });

        add(headerPanel, BorderLayout.NORTH);

        // 2. 좌/우 패널 컨테이너
        JPanel centerContainer = new JPanel(new GridLayout(1, 2));
        centerContainer.setOpaque(false); // 배경 이미지가 보이도록 투명 설정

        // 3. 좌측 패널: 선택된 카테고리 표시
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false); // 배경 이미지가 보이도록 투명 설정
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 120, 50, 20));

        // 카테고리 이미지
        categoryImageLabel = new JLabel("카테고리를 선택하세요"); // 초기 텍스트 설정
        categoryImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        categoryImageLabel.setPreferredSize(new Dimension(800, 800));
        categoryImageLabel.setMaximumSize(new Dimension(1000, 1000));
        categoryImageLabel.setBackground(new Color(240, 240, 245));
        categoryImageLabel.setOpaque(true);
        categoryImageLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        categoryImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoryImageLabel.setFont(FontManager.getDungGeunMo(16f)); // 폰트 설정

        // 카테고리 이름
        categoryNameLabel = new JLabel("");
        categoryNameLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 24f));
        categoryNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoryNameLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        categoryNameLabel.setOpaque(false); // 투명 설정

        leftPanel.add(categoryImageLabel);
        leftPanel.add(categoryNameLabel);

        centerContainer.add(leftPanel);

        // 4. 우측 패널: 토너먼트 규모 선택
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false); // 배경 이미지가 보이도록 투명 설정
        rightPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        // 라운드 버튼들
        gbc.insets = new Insets(10, 0, 10, 0);
        int[] rounds = {4, 8, 16, 32, 64};
        for (int i = 0; i < rounds.length; i++) {
            int round = rounds[i];
            RoundedButton roundButton = createRoundButton(round + " Round", round);
            roundButtons.put(round, roundButton);
            gbc.gridy = i + 1;
            rightPanel.add(roundButton, gbc);
        }

        // START 버튼 (배경색과 테두리 모두 #F17197, 텍스트는 흰색)
        Color startBgColor = PINK_COLOR; // #F17197
        Color startBorderColor = PINK_COLOR; // #F17197
        Color startHoverColor = new Color(220, 90, 130); // 호버 시 약간 어두운 핑크
        Color startTextColor = Color.WHITE;

        startButton = new RoundedButton("START \u2192", startBgColor, startBorderColor, startHoverColor, startTextColor);
        startButton.setFont(FontManager.getPressStart2P(20f));
        startButton.setPreferredSize(new Dimension(400, 70));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = rounds.length + 1;
        gbc.insets = new Insets(40, 0, 0, 0);
        rightPanel.add(startButton, gbc);

        centerContainer.add(rightPanel);
        add(centerContainer, BorderLayout.CENTER);
    }

    /**
     * 라운드 버튼을 생성합니다.
     */
    private RoundedButton createRoundButton(String text, int round) {
        RoundedButton button = new RoundedButton(text);
        button.setFont(FontManager.getPressStart2P(20f));
        button.setPreferredSize(new Dimension(400, 70));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            selectRound(round);
        });

        return button;
    }

    /**
     * 라운드를 선택합니다.
     */
    private void selectRound(int round) {
        // 라운드 선택 저장
        selectedRound = round;

        // START 버튼 활성화
        startButton.setEnabled(true);
    }

    /**
     * 선택된 카테고리를 설정하고 화면에 표시합니다.
     */
    public void setSelectedCategory(Category category) {
        this.selectedCategory = category;
        categoryNameLabel.setText(category.getName());

        // 이미지 로드 시도
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/" + category.getImagePath()));
            if (icon.getIconWidth() > 0) {
                Image scaledImage = icon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
                categoryImageLabel.setIcon(new ImageIcon(scaledImage));
                categoryImageLabel.setText("");
            } else {
                categoryImageLabel.setText("이미지 없음");
                categoryImageLabel.setFont(FontManager.getDungGeunMo(16f));
            }
        } catch (Exception e) {
            categoryImageLabel.setText("이미지 없음");
            categoryImageLabel.setFont(FontManager.getDungGeunMo(16f));
        }
    }

    /**
     * START 버튼에 액션 리스너를 추가합니다.
     */
    public void addStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    /**
     * 헤더(♥️ Pick Me) 클릭 리스너를 설정합니다.
     * @param listener 헤더 클릭 리스너
     */
    public void setBackButtonListener(Runnable listener) {
        this.backButtonListener = listener;
    }

    /**
     * 선택된 라운드를 반환합니다.
     */
    public int getSelectedRound() {
        return selectedRound;
    }

    /**
     * 선택된 카테고리를 반환합니다.
     */
    public Category getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * 패널을 초기화합니다.
     */
    public void reset() {
        selectedRound = 0;
        selectedCategory = null;
        startButton.setEnabled(false);

        // 카테고리 정보 초기화
        categoryNameLabel.setText("");
        categoryImageLabel.setIcon(null);
        categoryImageLabel.setText("카테고리를 선택하세요");
        categoryImageLabel.setFont(FontManager.getDungGeunMo(16f));
    }

    /**
     * 둥근 모서리 버튼 클래스
     */
    private static class RoundedButton extends JButton {
        private static final Color DEFAULT_BUTTON_BG = new Color(255, 243, 253); // #FFF3FD
        private static final Color DEFAULT_BORDER_COLOR = new Color(241, 113, 151); // #F17197
        private static final Color DEFAULT_HOVER_COLOR = new Color(255, 230, 245); // 호버 시 약간 어두운 색
        private static final int BORDER_WIDTH = 5;
        private static final int ARC_WIDTH = 80; // 둥글기 정도

        private Color buttonBg;
        private Color borderColor;
        private Color hoverColor;
        private boolean isHovered = false;

        // 기본 생성자 (라운드 버튼용)
        public RoundedButton(String text) {
            this(text, DEFAULT_BUTTON_BG, DEFAULT_BORDER_COLOR, DEFAULT_HOVER_COLOR, DEFAULT_BORDER_COLOR);
        }

        // 색상 커스터마이징 생성자 (START 버튼용)
        public RoundedButton(String text, Color buttonBg, Color borderColor, Color hoverColor, Color textColor) {
            super(text);
            this.buttonBg = buttonBg;
            this.borderColor = borderColor;
            this.hoverColor = hoverColor;

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(textColor);

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
                g2.setColor(hoverColor);
            } else {
                g2.setColor(buttonBg);
            }
            g2.fillRoundRect(0, 0, width - 1, height - 1, ARC_WIDTH, ARC_WIDTH);

            // 테두리 (5px)
            g2.setColor(borderColor);
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