package home.edu.vn.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import home.edu.vn.ui.AccountManagementUI;

public class MainScreenUI extends JFrame {
	JLabel lblIntroducePic, lblIntroduce, lblAccountPic, lblAccountManagement,
			lblReturnPic, lblStatisticsReturn, lblReportAssetPic,
			lblReportAsset, lblStockTradingPic, lblStockTrading,
			lblRechargeWithdrawMoneyPic, lblRechargeWithdrawMoney, 
			lblSupportPic, lblSupport, lblExitPic, lblExit;

	public MainScreenUI(String title) {
		super(title);
		addControls();
		addEvents();
	}

	private void addEvents() {
	
		lblAccountPic.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				handlingAccountManagement();
			}
		});
		
		lblAccountManagement.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				handlingAccountManagement();
			}
		});

	}

	protected void handlingAccountManagement() {
		AccountManagementUI ui=new AccountManagementUI("Quản lý tài khoản");
		ui.showWindow();
	}

	private void addControls() {
		Container con = getContentPane();
		con.setLayout(new BorderLayout());

		JPanel pnPortfolioManagement = new JPanel();
		pnPortfolioManagement.setLayout(new BorderLayout());
		con.add(pnPortfolioManagement);

		JPanel pnNorth = new JPanel();
		pnNorth.setPreferredSize(new Dimension(0, 180));
		JPanel pnSouth = new JPanel();
		pnSouth.setPreferredSize(new Dimension(0, 50));
		JPanel pnWest = new JPanel();
		pnWest.setPreferredSize(new Dimension(100, 0));
		JPanel pnEast = new JPanel();
		pnEast.setPreferredSize(new Dimension(100, 0));
		JPanel pnCenter = new JPanel();

		pnPortfolioManagement.add(pnNorth, BorderLayout.NORTH);
		pnPortfolioManagement.add(pnSouth, BorderLayout.SOUTH);
		pnPortfolioManagement.add(pnWest, BorderLayout.WEST);
		pnPortfolioManagement.add(pnEast, BorderLayout.EAST);
		pnPortfolioManagement.add(pnCenter, BorderLayout.CENTER);

		Border borderTitle = BorderFactory.createLineBorder(Color.GRAY);
		TitledBorder titledborderTitle = new TitledBorder(borderTitle,
				"Quản lý danh mục đầu tư");
		pnPortfolioManagement.setBorder(titledborderTitle);
		titledborderTitle.setTitleColor(Color.BLUE);
		titledborderTitle.setTitleJustification(TitledBorder.CENTER);

		JLabel lblBackground = new JLabel();
		ImageIcon imgBackground = new ImageIcon("images/stockmarket.png");
		lblBackground.setIcon(imgBackground);
		lblBackground.setPreferredSize(new Dimension(750, 150));
		pnNorth.add(lblBackground);

		JPanel pnLeftOfCenter = new JPanel();
		pnLeftOfCenter.setLayout(new BoxLayout(pnLeftOfCenter, BoxLayout.Y_AXIS));
		JPanel pnRightOfCenter = new JPanel();
		pnRightOfCenter.setLayout(new BoxLayout(pnRightOfCenter,BoxLayout.Y_AXIS));
		pnCenter.add(pnLeftOfCenter);
		pnCenter.add(pnRightOfCenter);

		JPanel pnLeftOfCenter1 = new JPanel();
		pnLeftOfCenter1.setLayout(new FlowLayout(FlowLayout.LEFT));
		lblIntroducePic = new JLabel();
		ImageIcon imgIntroducePic = new ImageIcon("images/guide.png");
		lblIntroducePic.setIcon(imgIntroducePic);
		lblIntroducePic.setPreferredSize(new Dimension(64, 64));
		lblIntroduce = new JLabel("Giới thiệu & Hướng dẫn");

		pnLeftOfCenter1.add(lblIntroducePic);
		pnLeftOfCenter1.add(lblIntroduce);
		pnLeftOfCenter.add(pnLeftOfCenter1);

		JPanel pnLeftOfCenter2 = new JPanel();
		pnLeftOfCenter2.setLayout(new FlowLayout(FlowLayout.LEFT));
		lblAccountPic = new JLabel();
		ImageIcon imgAccount = new ImageIcon("images/user.png");
		lblAccountPic.setIcon(imgAccount);
		lblAccountPic.setPreferredSize(new Dimension(64, 64));
		lblAccountManagement = new JLabel("Quản lý tài khoản");

		pnLeftOfCenter2.add(lblAccountPic);
		pnLeftOfCenter2.add(lblAccountManagement);
		pnLeftOfCenter.add(pnLeftOfCenter2);

		JPanel pnLeftOfCenter3 = new JPanel();
		pnLeftOfCenter3.setLayout(new FlowLayout(FlowLayout.LEFT));
		lblReturnPic = new JLabel();
		ImageIcon imgReturn = new ImageIcon("images/thongke.png");
		lblReturnPic.setIcon(imgReturn);
		lblReturnPic.setPreferredSize(new Dimension(64, 64));
		lblStatisticsReturn = new JLabel("Thống kê Return tháng & năm");

		pnLeftOfCenter3.add(lblReturnPic);
		pnLeftOfCenter3.add(lblStatisticsReturn);
		pnLeftOfCenter.add(pnLeftOfCenter3);

		JPanel pnLeftOfCenter4 = new JPanel();
		pnLeftOfCenter4.setLayout(new FlowLayout(FlowLayout.LEFT));
		lblReportAssetPic = new JLabel();
		ImageIcon imgReportAsset = new ImageIcon("images/report.png");
		lblReportAssetPic.setIcon(imgReportAsset);
		lblReportAssetPic.setPreferredSize(new Dimension(64, 64));
		lblReportAsset = new JLabel("Báo cáo tài sản");

		pnLeftOfCenter4.add(lblReportAssetPic);
		pnLeftOfCenter4.add(lblReportAsset);
		pnLeftOfCenter.add(pnLeftOfCenter4);

		JPanel pnRightOfCenter1 = new JPanel();
		pnRightOfCenter1.setLayout(new FlowLayout(FlowLayout.LEFT));
		lblStockTradingPic = new JLabel();
		ImageIcon imgStock = new ImageIcon("images/cart.png");
		lblStockTradingPic.setIcon(imgStock);
		lblStockTradingPic.setPreferredSize(new Dimension(64, 64));
		lblStockTrading = new JLabel("Lệnh mua/bán chứng khoán");

		pnRightOfCenter1.add(lblStockTradingPic);
		pnRightOfCenter1.add(lblStockTrading);
		pnRightOfCenter.add(pnRightOfCenter1);

		JPanel pnRightOfCenter2 = new JPanel();
		pnRightOfCenter2.setLayout(new FlowLayout(FlowLayout.LEFT));
		lblRechargeWithdrawMoneyPic = new JLabel();
		ImageIcon imgMoney = new ImageIcon("images/money.png");
		lblRechargeWithdrawMoneyPic.setIcon(imgMoney);
		lblRechargeWithdrawMoneyPic.setPreferredSize(new Dimension(64, 64));
		lblRechargeWithdrawMoney = new JLabel("Lệnh nạp/rút tiền");

		pnRightOfCenter2.add(lblRechargeWithdrawMoneyPic);
		pnRightOfCenter2.add(lblRechargeWithdrawMoney);
		pnRightOfCenter.add(pnRightOfCenter2);

		JPanel pnRightOfCenter3 = new JPanel();
		pnRightOfCenter3.setLayout(new FlowLayout(FlowLayout.LEFT));
		lblSupportPic = new JLabel();
		ImageIcon imgSupport = new ImageIcon("images/support.png");
		lblSupportPic.setIcon(imgSupport);
		lblSupportPic.setPreferredSize(new Dimension(64, 64));
		lblSupport = new JLabel("Hỗ trợ");

		pnRightOfCenter3.add(lblSupportPic);
		pnRightOfCenter3.add(lblSupport);
		pnRightOfCenter.add(pnRightOfCenter3);

		JPanel pnRightOfCenter4 = new JPanel();
		pnRightOfCenter4.setLayout(new FlowLayout(FlowLayout.LEFT));
		lblExitPic = new JLabel();
		ImageIcon imgExit = new ImageIcon("images/logout.png");
		lblExitPic.setIcon(imgExit);
		lblExitPic.setPreferredSize(new Dimension(64, 64));
		lblExit = new JLabel("Thoát chương trình");

		pnRightOfCenter4.add(lblExitPic);
		pnRightOfCenter4.add(lblExit);
		pnRightOfCenter.add(pnRightOfCenter4);

	}

	public void showWindow() {
		this.setSize(750, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
}
