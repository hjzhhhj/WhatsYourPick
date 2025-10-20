package com.whatsyourpick.ui;

import com.whatsyourpick.model.Contestant;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.io.File;
import java.net.URL;

/**
 * 대결 화면 패널
 * 두 대상을 표시하고 사용자가 선택할 수 있도록 합니다.
 */
public class BattlePanel extends JPanel {

    private JLabel roundInfoLabel;
    private JLabel matchCountLabel;
    private JPanel leftContestantPanel;
    private JPanel rightContestantPanel;
    private JLabel leftImageLabel;
    private JLabel rightImageLabel;
    private JLabel leftNameLabel;
    private JLabel rightNameLabel;
    private Consumer<Contestant> winnerSelectListener;
    private Contestant leftContestant;
    private Contestant rightContestant;

    public BattlePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        initComponents();
    }

    private void initComponents() {
        // 상단 정보 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        topPanel.setBackground(new Color(245, 245, 250));

        roundInfoLabel = new JLabel("");
        roundInfoLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 28f));
        roundInfoLabel.setForeground(new Color(75, 0, 130));

        matchCountLabel = new JLabel("");
        matchCountLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 22f));
        matchCountLabel.setForeground(new Color(100, 100, 100));

        topPanel.add(roundInfoLabel);
        topPanel.add(matchCountLabel);
        add(topPanel, BorderLayout.NORTH);

        // 중앙 대결 패널
        JPanel battlePanel = new JPanel(new GridBagLayout());
        battlePanel.setBackground(new Color(245, 245, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        // 왼쪽 대상
        leftContestantPanel = createContestantPanel(true);
        gbc.gridx = 0;
        battlePanel.add(leftContestantPanel, gbc);

        // VS 텍스트
        JLabel vsLabel = new JLabel("VS");
        vsLabel.setFont(FontManager.getPressStart2P(Font.BOLD, 48f));
        vsLabel.setForeground(new Color(220, 20, 60));
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 40, 20, 40);
        battlePanel.add(vsLabel, gbc);

        // 오른쪽 대상
        rightContestantPanel = createContestantPanel(false);
        gbc.gridx = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        battlePanel.add(rightContestantPanel, gbc);

        add(battlePanel, BorderLayout.CENTER);
    }

    /**
     * 대상 패널을 생성합니다.
     */
    private JPanel createContestantPanel(boolean isLeft) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(350, 450));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 3));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 이미지 레이블
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(350, 350));
        imageLabel.setBackground(new Color(240, 240, 245));
        imageLabel.setOpaque(true);
        panel.add(imageLabel, BorderLayout.CENTER);

        // 이름 레이블
        JLabel nameLabel = new JLabel("", SwingConstants.CENTER);
        nameLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 20f));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        nameLabel.setOpaque(true);
        nameLabel.setBackground(Color.WHITE);
        panel.add(nameLabel, BorderLayout.SOUTH);

        // 호버 효과
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(240, 240, 255));
                nameLabel.setBackground(new Color(240, 240, 255));
                panel.setBorder(BorderFactory.createLineBorder(new Color(75, 0, 130), 4));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
                nameLabel.setBackground(Color.WHITE);
                panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 3));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (winnerSelectListener != null) {
                    Contestant winner = isLeft ? leftContestant : rightContestant;
                    if (winner != null) {
                        winnerSelectListener.accept(winner);
                    }
                }
            }
        });

        // 레이블 참조 저장
        if (isLeft) {
            leftImageLabel = imageLabel;
            leftNameLabel = nameLabel;
        } else {
            rightImageLabel = imageLabel;
            rightNameLabel = nameLabel;
        }

        return panel;
    }

    /**
     * 대결 정보를 설정합니다.
     */
    public void setBattle(Contestant left, Contestant right, String roundName, String matchInfo) {
        this.leftContestant = left;
        this.rightContestant = right;

        // 라운드 정보 설정
        roundInfoLabel.setText(roundName);
        matchCountLabel.setText(matchInfo);

        // 왼쪽 대상 설정
        leftNameLabel.setText(left.getName());
        loadImage(leftImageLabel, left.getImagePath(), left.getName());

        // 오른쪽 대상 설정
        rightNameLabel.setText(right.getName());
        loadImage(rightImageLabel, right.getImagePath(), right.getName());
        
        System.out.println("🥊 대결: " + left.getName() + " VS " + right.getName());
    }

    /**
     * 이미지를 로드합니다.
     */
    private void loadImage(JLabel label, String imagePath, String name) {
        try {
            // 경로 정리 (맨 앞의 / 제거)
            String cleanPath = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;
            
            System.out.println("🖼️  이미지 로드 시도: " + cleanPath + " (이름: " + name + ")");
            
            // 리소스에서 이미지 로드 시도
            URL imageUrl = getClass().getClassLoader().getResource(cleanPath);
            
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                
                if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                    // 이미지를 350x350 크기로 조정
                    Image scaledImage = icon.getImage().getScaledInstance(
                        350, 350, Image.SCALE_SMOOTH
                    );
                    label.setIcon(new ImageIcon(scaledImage));
                    label.setText("");
                    System.out.println("✅ 이미지 로드 성공: " + name);
                } else {
                    // 아이콘 크기가 0인 경우
                    setImageNotFound(label, name, "아이콘 크기 0");
                }
            } else {
                // URL이 null인 경우
                setImageNotFound(label, name, "리소스를 찾을 수 없음: " + cleanPath);
            }
            
        } catch (Exception e) {
            setImageNotFound(label, name, e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 이미지를 찾을 수 없을 때 표시
     */
    private void setImageNotFound(JLabel label, String name, String reason) {
        label.setIcon(null);
        label.setText("<html><center>이미지 없음<br><small>" + name + "</small></center></html>");
        label.setFont(FontManager.getDungGeunMo(14f));
        label.setForeground(new Color(150, 150, 150));
        System.err.println("❌ 이미지 로드 실패: " + name + " - " + reason);
    }

    /**
     * 승자 선택 리스너를 설정합니다.
     */
    public void setWinnerSelectListener(Consumer<Contestant> listener) {
        this.winnerSelectListener = listener;
    }
}