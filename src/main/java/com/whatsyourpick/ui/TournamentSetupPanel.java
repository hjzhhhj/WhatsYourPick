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
    private Map<Integer, JButton> roundButtons;
    private JButton startButton;
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
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 20));

        // 카테고리 이미지
        categoryImageLabel = new JLabel("카테고리를 선택하세요"); // 초기 텍스트 설정
        categoryImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        categoryImageLabel.setPreferredSize(new Dimension(300, 300));
        categoryImageLabel.setMaximumSize(new Dimension(300, 300));
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
            JButton roundButton = createRoundButton(round + "강", round);
            roundButtons.put(round, roundButton);
            gbc.gridy = i + 1;
            rightPanel.add(roundButton, gbc);
        }

        // START 버튼
        startButton = new JButton("START \u2192");
        startButton.setFont(FontManager.getPressStart2P(18f));
        startButton.setPreferredSize(new Dimension(320, 60));
        startButton.setBackground(new Color(150, 150, 150)); // 비활성화 색상
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        startButton.setEnabled(false);
        gbc.gridy = rounds.length + 1;
        gbc.insets = new Insets(40, 0, 0, 0);
        rightPanel.add(startButton, gbc);

        centerContainer.add(rightPanel);
        add(centerContainer, BorderLayout.CENTER);
    }

    /**
     * 라운드 버튼을 생성합니다.
     */
    private JButton createRoundButton(String text, int round) {
        JButton button = new JButton(text);
        button.setFont(FontManager.getDungGeunMo(Font.BOLD, 22f));
        button.setPreferredSize(new Dimension(320, 55));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            selectRound(round);
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (selectedRound != round) {
                    button.setBackground(new Color(240, 240, 250));
                    button.setBorder(BorderFactory.createLineBorder(new Color(75, 0, 130), 2));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (selectedRound != round) {
                    button.setBackground(Color.WHITE);
                    button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
                }
            }
        });

        return button;
    }

    /**
     * 라운드를 선택합니다.
     */
    private void selectRound(int round) {
        // 이전 선택 해제
        if (selectedRound > 0 && roundButtons.containsKey(selectedRound)) {
            JButton prevButton = roundButtons.get(selectedRound);
            prevButton.setBackground(Color.WHITE);
            prevButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        }

        // 새로운 선택
        selectedRound = round;
        JButton currentButton = roundButtons.get(round);
        currentButton.setBackground(new Color(75, 0, 130));
        currentButton.setForeground(Color.WHITE);
        currentButton.setBorder(BorderFactory.createLineBorder(new Color(75, 0, 130), 2));

        // START 버튼 활성화
        startButton.setEnabled(true);
        startButton.setBackground(new Color(75, 0, 130));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // START 버튼 호버 리스너는 선택 시 한 번만 등록하도록 처리 (중복 등록 방지)
        if (startButton.getMouseListeners().length == 0) {
            startButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    startButton.setBackground(new Color(100, 20, 160));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    startButton.setBackground(new Color(75, 0, 130));
                }
            });
        }
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
                Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
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
        startButton.setBackground(new Color(150, 150, 150));

        // START 버튼의 마우스 리스너 제거 (비활성화 상태에서 호버 효과 방지)
        for (java.awt.event.MouseListener listener : startButton.getMouseListeners()) {
            startButton.removeMouseListener(listener);
        }

        for (JButton button : roundButtons.values()) {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
            button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        }

        // 카테고리 정보 초기화
        categoryNameLabel.setText("");
        categoryImageLabel.setIcon(null);
        categoryImageLabel.setText("카테고리를 선택하세요");
        categoryImageLabel.setFont(FontManager.getDungGeunMo(16f));
    }
}