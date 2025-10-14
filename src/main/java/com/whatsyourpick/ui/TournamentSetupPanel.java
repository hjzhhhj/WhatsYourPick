package com.whatsyourpick.ui;

import com.whatsyourpick.model.Category;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * 토너먼트 설정 화면 패널
 * 선택한 카테고리와 토너먼트 규모를 표시합니다.
 */
public class TournamentSetupPanel extends JPanel {

    private JLabel categoryImageLabel;
    private JLabel categoryNameLabel;
    private Map<Integer, JButton> roundButtons;
    private JButton startButton;
    private int selectedRound = 0;
    private Category selectedCategory;

    public TournamentSetupPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        roundButtons = new HashMap<>();
        initComponents();
    }

    private void initComponents() {
        // 좌측 패널: 선택된 카테고리 표시
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(245, 245, 250));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 20));

        // 카테고리 이미지
        categoryImageLabel = new JLabel();
        categoryImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        categoryImageLabel.setPreferredSize(new Dimension(300, 300));
        categoryImageLabel.setMaximumSize(new Dimension(300, 300));
        categoryImageLabel.setBackground(new Color(240, 240, 245));
        categoryImageLabel.setOpaque(true);
        categoryImageLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        categoryImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 카테고리 이름
        categoryNameLabel = new JLabel("");
        categoryNameLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 24f));
        categoryNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoryNameLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        leftPanel.add(categoryImageLabel);
        leftPanel.add(categoryNameLabel);

        add(leftPanel, BorderLayout.WEST);

        // 우측 패널: 토너먼트 규모 선택
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(245, 245, 250));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        // 타이틀
        JLabel titleLabel = new JLabel("Select Tournament Size");
        titleLabel.setFont(FontManager.getPressStart2P(Font.BOLD, 28f));
        titleLabel.setForeground(new Color(75, 0, 130));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        rightPanel.add(titleLabel, gbc);

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

        add(rightPanel, BorderLayout.CENTER);
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
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(100, 20, 160));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(75, 0, 130));
            }
        });
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

        for (JButton button : roundButtons.values()) {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
            button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        }
    }
}