package com.whatsyourpick.ui;

import com.whatsyourpick.model.Category;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;

/**
 * 카테고리 선택 화면 패널
 * 9개 카테고리를 3x3 그리드로 표시합니다.
 */
public class CategoryPanel extends JPanel {

    private Consumer<Category> categorySelectListener;
    private JPanel gridPanel;

    public CategoryPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250));
        initComponents();
    }

    private void initComponents() {
        // 타이틀
        JLabel titleLabel = new JLabel("Choose Your Category", SwingConstants.CENTER);
        titleLabel.setFont(FontManager.getPressStart2P(Font.BOLD, 32f));
        titleLabel.setForeground(new Color(75, 0, 130));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 20, 30, 20));
        add(titleLabel, BorderLayout.NORTH);

        // 카테고리 그리드
        gridPanel = new JPanel(new GridLayout(3, 3, 20, 20));
        gridPanel.setBackground(new Color(245, 245, 250));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));
        add(gridPanel, BorderLayout.CENTER);
    }

    /**
     * 카테고리 목록을 표시합니다.
     * @param categories 카테고리 리스트
     */
    public void displayCategories(List<Category> categories) {
        gridPanel.removeAll();

        for (Category category : categories) {
            JPanel categoryCard = createCategoryCard(category);
            gridPanel.add(categoryCard);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    /**
     * 카테고리 카드를 생성합니다.
     * @param category 카테고리
     * @return 카테고리 카드 패널
     */
    private JPanel createCategoryCard(Category category) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 이미지 영역 (실제 이미지는 나중에 추가)
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 200));
        imageLabel.setBackground(new Color(240, 240, 245));
        imageLabel.setOpaque(true);

        // 이미지 로드 시도
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/" + category.getImagePath()));
            if (icon.getIconWidth() > 0) {
                Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imageLabel.setText("이미지 없음");
                imageLabel.setFont(FontManager.getDungGeunMo(14f));
            }
        } catch (Exception e) {
            imageLabel.setText("이미지 없음");
            imageLabel.setFont(FontManager.getDungGeunMo(14f));
        }

        card.add(imageLabel, BorderLayout.CENTER);

        // 카테고리 이름
        JLabel nameLabel = new JLabel(category.getName(), SwingConstants.CENTER);
        nameLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 18f));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        nameLabel.setOpaque(true);
        nameLabel.setBackground(Color.WHITE);
        card.add(nameLabel, BorderLayout.SOUTH);

        // 호버 효과 및 클릭 이벤트
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(240, 240, 250));
                nameLabel.setBackground(new Color(240, 240, 250));
                card.setBorder(BorderFactory.createLineBorder(new Color(75, 0, 130), 3));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                nameLabel.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (categorySelectListener != null) {
                    categorySelectListener.accept(category);
                }
            }
        });

        return card;
    }

    /**
     * 카테고리 선택 리스너를 설정합니다.
     * @param listener 카테고리 선택 리스너
     */
    public void setCategorySelectListener(Consumer<Category> listener) {
        this.categorySelectListener = listener;
    }
}