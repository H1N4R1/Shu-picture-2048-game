import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Picture2048Game extends JPanel {
	int[][] board = new int[4][4];
	Image img2, img4, img8, img16, img32, img64, img128, img256;
	
	int mergeScale= 100;
	int mergeRow = -1;
	int mergeCol = -1;
	
	javax.swing.JButton backButton;
	boolean gameClear = false;
	boolean gameOver = false;
	int clearFlash = 0;
	
	public Picture2048Game() {
		img2 = new ImageIcon("images/2.png").getImage();
		img4 = new ImageIcon("images/4.png").getImage();
		img8 = new ImageIcon("images/8.png").getImage();
		img16 = new ImageIcon("images/16.png").getImage();
		img32 = new ImageIcon("images/32.png").getImage();
		img64 = new ImageIcon("images/64.png").getImage();
		img128 = new ImageIcon("images/128.png").getImage();
		img256 = new ImageIcon("images/256.png").getImage();
		
	    setBackground(Color.WHITE);
	    setFocusable(true);   //このパネルはキーボード入力を受け取れますよ宣言
	    setLayout(null);
	    
	    placeRandomTwo();
	    placeRandomTwo();
	    
	 // テスト用
	  //  board[0][0] = 128;
	  //  board[0][1] = 128;

	    addKeyListener(new KeyAdapter() {
	    	
	        public void keyPressed(KeyEvent e) {
	        	
	        

	        	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	        	    moveLeft();
	        	}
	        	
	        	if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	        	    moveRight();
	        	}
	        	
	        	if (e.getKeyCode() == KeyEvent.VK_UP) {
	        	    moveUp();
	        	}
	        	
	        	if (e.getKeyCode() == KeyEvent.VK_DOWN) {
	        	    moveDown();
	        	}

	        }
	    });
	    
	    backButton = new javax.swing.JButton("もう１回") {
	        @Override
	        protected void paintComponent(Graphics g) {
	            Graphics2D g2 = (Graphics2D) g.create(); // ← ここで g2 を作る

	            // 背景色
	            g2.setColor(new Color(50, 50, 50));
	            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

	            // 枠線（太くする）
	            g2.setColor(Color.WHITE);
	            g2.setStroke(new BasicStroke(4));
	            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);

	            g2.dispose(); // コピー破棄

	            // 文字
	            super.paintComponent(g);  
	        }

	        @Override
	        public boolean isContentAreaFilled() {
	            return false; 
	        }

	        @Override
	        public boolean isBorderPainted() {
	            return false; 
	        }
	    };
	    backButton.setBounds(150, 260, 160, 40);
	    backButton.setVisible(false); // 最初は隠す
	    
	 // ← ここで文字色を白にする
	    backButton.setForeground(Color.WHITE);
	    
	    add(backButton);
	    
	 // ===== テスト用：すぐゲームクリア画面を見る =====
	 		//gameClear = true;   // ← この行を消せば通常ゲームに戻る
	    //gameOver = true;
	 		//backButton.setVisible(true);
	 		// ===============================================
	    
	    backButton.addActionListener(e -> resetGame());
	}
	
    
    private void placeRandomTwo() {

        int row;
        int col;

        do {
            row = (int)(Math.random() * 4);
            col = (int)(Math.random() * 4);
        } 	while (board[row][col] != 0);

        board[row][col] = 2;
    }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int cellSize = 100;
            int margin = 10;

            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){

                    int x = margin + j * (cellSize + margin);
                    int y = margin + i * (cellSize + margin);
                    

                    //四角を描く
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(x, y, cellSize, cellSize);
                    
                    //今見てるマスの数字を取り出す
                    int value = board[i][j];
                    
                    int size = cellSize;
                    int drawX = x;
                    int drawY = y;

                    if(i == mergeRow && j == mergeCol){
                        size = mergeScale;
                   
                        int offset = (cellSize - mergeScale) / 2;
                        drawX = x + offset;
                        drawY = y + offset;
                    }
                    
                    if(value == 2) g.drawImage(img2, drawX, drawY, size, size, null);
                    if(value == 4) g.drawImage(img4, drawX, drawY, size, size, null);
                    if(value == 8) g.drawImage(img8, drawX, drawY, size, size, null);
                    if(value == 16) g.drawImage(img16, drawX, drawY, size, size, null);
                    if(value == 32) g.drawImage(img32, drawX, drawY, size, size, null);
                    if(value == 64) g.drawImage(img64, drawX, drawY, size, size, null);
                    if(value == 128) g.drawImage(img128, drawX, drawY, size, size, null);
                    if(value == 256) g.drawImage(img256, drawX, drawY, size, size, null);
                    
                    if(value == 256 && clearFlash > 0){
                        g.setColor(new Color(255,255,255,150));
                        g.fillRect(x, y, cellSize, cellSize);
                    }
                    
                }
            }
            
            			if (gameClear) {
            				g.setColor(new Color(0,0,0,150));
            			    g.fillRect(0, 0, getWidth(), getHeight());

            			    g.setColor(Color.WHITE);
            			    g.setFont(new Font("ＭＳ Ｐゴシック", Font.BOLD, 36));
            			    
            			    String mainMessage = "♦ゲームクリア♦";      // 描く文字列
            			    FontMetrics fm = g.getFontMetrics();     // 現在のフォントで文字幅取得
            			    int mainWidth = fm.stringWidth(mainMessage);  // 文字幅（int型）を計算
            			    int mainX = (getWidth() - mainWidth) / 2;    // 横中央
            			    int mainY = 180;                             // 上寄りの縦位置
            			    g.drawString(mainMessage, mainX, mainY); 

            			}
            			
            			if(clearFlash > 0){
            		        clearFlash--;
            		        
            		        if(clearFlash == 0){
            		            gameClear = true;
            		            backButton.setVisible(true);
            		        }
            		        
            		        repaint();
            		    }
            			
            			if (gameOver) {
            			    g.setColor(new Color(0,0,0,150));
            			    g.fillRect(0, 0, getWidth(), getHeight());
            			    

            			    g.setColor(Color.WHITE);
            			    g.setFont(new Font("ＭＳ Ｐゴシック", Font.BOLD, 36));

            			    String mainMessage = "ゲーム失敗…";
            			    FontMetrics fm = g.getFontMetrics();
            			    int mainWidth = fm.stringWidth(mainMessage);
            			    int mainX = (getWidth() - mainWidth) / 2;
            			    int mainY = 180;
            			    g.drawString(mainMessage, mainX, mainY);

            			}
            			
            			// 合体演出の管理をリセット（常に通常サイズにする）
            			mergeRow = -1;
            			mergeCol = -1;
            			mergeScale = 100;
            			
        }
        
        //左に移動
        private void moveLeft() {

            boolean moved = false;

            for (int i = 0; i < 4; i++) {

                int[] originalRow = board[i].clone();

                int[] newRow = moveRowLeft(originalRow);
                
               // mergeRow = i;
                
                for (int j = 0; j < 4; j++) {
                    if (originalRow[j] != newRow[j] && newRow[j] != 0) {
                       
                    }
                }

                board[i] = newRow;

                for (int j = 0; j < 4; j++) {
                    if (originalRow[j] != newRow[j]) {
                        moved = true;
                    }
                }
            }

            if (moved) {
                placeRandomTwo();
                checkClear();
            }

            repaint();
        }
        
        private int[] moveRowLeft(int[] row) {
            int[] newRow = new int[4];
            int index = 0;

            // ① 詰める
            for (int j = 0; j < 4; j++) {
                if (row[j] != 0) {
                    newRow[index++] = row[j];
                }
            }

            // ② 合体
            for (int j = 0; j < 3; j++) {
                if (newRow[j] == newRow[j+1] && newRow[j] != 0) {
                    newRow[j] *= 2;
                    newRow[j+1] = 0;
                    
                   // mergeScale = 20;
                    mergeCol = j;

                    // ここでチェックして即表示
                    if (newRow[j] == 256) {
                    	
                    	clearFlash = 40;
                    	
                    }
                }
            }

            // ③ 再詰め
            int[] finalRow = new int[4];
            index = 0;
            for (int j = 0; j < 4; j++) {
                if (newRow[j] != 0) {
                    finalRow[index++] = newRow[j];
                }
            }

            return finalRow;
        }
        
        //右に移動
        private void moveRight() {

            boolean moved = false;

            for (int i = 0; i < 4; i++) {

                int[] originalRow = board[i].clone();

                int[] newRow = moveRowRight(originalRow);

                board[i] = newRow;

                for (int j = 0; j < 4; j++) {
                    if (originalRow[j] != newRow[j]) {
                        moved = true;
                    }
                }
            }

            if (moved) {
                placeRandomTwo();
                checkClear();
            }

            repaint();
        }
        
        private int[] moveRowRight(int[] row) {

            int[] reversed = new int[4];

            // ① 逆にする
            for (int i = 0; i < 4; i++) {
                reversed[i] = row[3 - i];
            }

            // ② 左移動ロジックを使う
            int[] moved = moveRowLeft(reversed);

            int[] result = new int[4];

            // ③ もう一度逆に戻す
            for (int i = 0; i < 4; i++) {
                result[i] = moved[3 - i];
            }

            return result;
        }
        
        //上に移動
        private void moveUp() {

            boolean moved = false;

            for (int col = 0; col < 4; col++) {

                int[] column = new int[4];

                // ① 列を取り出す
                for (int row = 0; row < 4; row++) {
                    column[row] = board[row][col];
                }

                int[] originalColumn = column.clone();

                // ② 左ロジックを使う（＝上に詰める）
                int[] newColumn = moveRowLeft(column);

                // ③ 元に戻す
                for (int row = 0; row < 4; row++) {
                    board[row][col] = newColumn[row];
                }

                // ④ 動いたか判定
                for (int row = 0; row < 4; row++) {
                    if (originalColumn[row] != newColumn[row]) {
                        moved = true;
                    }
                }
            }

            if (moved) {
                placeRandomTwo();
                checkClear();
            }
            
            if (!gameClear) {
                checkGameOver();
            }

            repaint();
        }
        
        private void checkClear() {

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {

                    if (board[i][j] == 256) {
                    
                    }

                }
            }
        }
        
        
        //下に移動
        private void moveDown() {

            boolean moved = false;

            for (int col = 0; col < 4; col++) {

                int[] column = new int[4];

                // ① 列を取り出す
                for (int row = 0; row < 4; row++) {
                    column[row] = board[row][col];
                }

                int[] originalColumn = column.clone();

                // ② 逆にする（下→上扱いにするため）
                int[] reversed = new int[4];
                for (int i = 0; i < 4; i++) {
                    reversed[i] = column[3 - i];
                }

                // ③ 左ロジックを使う
                int[] movedColumn = moveRowLeft(reversed);

                // ④ もう一度逆に戻す
                int[] newColumn = new int[4];
                for (int i = 0; i < 4; i++) {
                    newColumn[i] = movedColumn[3 - i];
                }

                // ⑤ board に戻す
                for (int row = 0; row < 4; row++) {
                    board[row][col] = newColumn[row];
                }

                // ⑥ 動いたか判定
                for (int row = 0; row < 4; row++) {
                    if (originalColumn[row] != newColumn[row]) {
                        moved = true;
                    }
                }
            }

            if (moved) {
                placeRandomTwo();
                
            }

            repaint();
            
        }
        
        private void checkGameOver() {
            boolean full = true;

            // 空きマスがあるかチェック
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (board[i][j] == 0) {
                        full = false;
                    }
                }
            }
            
            if (full) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (board[i][j] == board[i][j+1] || board[j][i] == board[j+1][i]) {
                            full = false; // 合体できるならまだゲームオーバーじゃない
                        }
                    }
                }
            }

            if (full) {
                gameOver = true;
                backButton.setVisible(true); // クリアと同じボタンを使う
                repaint();
            }
        }
        
        private void resetGame() {

            board = new int[4][4];
            gameClear = false;
            gameOver = false;

            placeRandomTwo();
            placeRandomTwo();

            backButton.setVisible(false);

            repaint();
        }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Picture2048");

        Picture2048Game panel = new Picture2048Game();

        frame.add(panel);
        frame.setSize(470, 490);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}