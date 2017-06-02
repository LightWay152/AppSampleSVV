package home.edu.vn.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import home.edu.vn.model.Stock;
import home.edu.vn.model.Account;
import home.edu.vn.service.CheckEmailFormating;

public class AccountManagementUI extends JFrame {

	JList<Account> listAccount;//show list of soTK
	ArrayList<Stock>lstStock;//show stock table

	JTextField txtAccountName,txtYOB,txtMobile,
	txtIdentityCardNumber,txtAccountNumber,txtEmail,
	txtAddress,txtStartDay,txtStockCode,txtPrice,
	txtQuantity,txtCashBalance,txtTotalAssets;
	
	JTextField txtTotalStockValue;//using to calculate total assets

	DefaultTableModel dtmStock;
	JTable tblStock;

	JButton btnAddAccount,btnDeleteAccount,btnEditAccount,btnUpdateAccount,
			btnAddStock,btnDeleteStock,btnEditStock,btnUpdateStock,btnExit;

	Connection conn=null;
	PreparedStatement preStatement=null;
	Statement statement=null;
	ResultSet result=null;
	String strServer="DESKTOP-UBC0EDN\\SQLEXPRESS";
	String strDatabase="dbQuanLyDanhMucDauTu";

	public AccountManagementUI(String title)
	{
		super(title);
		addControls();
		addEvents();

		connectDatabase();
		displayListOfAccountNumber();

	}	
/*------------------- Start processing the data from the database ------------------*/
	
	private boolean checkPriceFormat(String price)
	{
		float checkPrice= Float.parseFloat(price);
		if (checkPrice==(float)checkPrice)
			return true;
		else
			return false;
	}

	private boolean checkQuantityFormat(String quantity)
	{
		int checkQuantity= Integer.parseInt(quantity);
		if (checkQuantity==(int)checkQuantity)
			return true;
		else
			return false;
	}
		
	public boolean checkAccountNumberInStockTableExist(String accountNumber)
	{ 
		try
		{
			String sql="select * from ChungKhoan where SoTK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, accountNumber);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean checkAccountNumberInAccountTableExist(String accountNumber)
	{
		try
		{
			String sql="select * from TaiKhoan where SoTK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, accountNumber);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean checkStockCodeByAccountNumberInStockTableExist2(
			String accountNumber,String stockCode)
	{
		try
		{
			String sql="select * from ChungKhoan where SoTK=? and MaVaChiSoCK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, accountNumber);
			preStatement.setString(2, stockCode);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean checkIdentityCardNumberInAccountTableExist(
			String identityCardNumber)
	{
		try
		{
			String sql="select * from TaiKhoan where SoCMND=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, identityCardNumber);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean checkMobileInAccountTableExist(String mobile)
	{
		try
		{
			String sql="select * from TaiKhoan where SoDT=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, mobile);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean checkEmailInAccountTableExist(String email)
	{
		try
		{
			String sql="select * from TaiKhoan where Email=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, email);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean checkStockCodeByAccountNumberInStockTableExist(
			String accountNumber,String stockCode)
	{
		try
		{
			String sql="select * from ChungKhoan where SoTK=? and MaVaChiSoCK=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, accountNumber);
			preStatement.setString(2, stockCode);
			ResultSet rs=preStatement.executeQuery();
			return rs.next();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	public boolean checkDayMonthYearFormat(String dayMonthYear)
	{
		if (dayMonthYear.matches("([0-9]{2})/([0-9]{2})/([1-9]{4})"))
			return true;
		else
			return false;
	}

	public boolean checkStringHasNumber(String stringToCheck)
	{	
		if(stringToCheck.matches("[0-9]+"))
			return true;//string has all numbers
		else
			return false;//string hasn't any number	
	}

	public boolean checkStringHasLeast1Number(String stringToCheck)
	{	
		if(stringToCheck.matches(".*\\d+.*"))
			return true;//string has at least 1 number
		else
			return false;//string hasn't any number			
	}

	public String formatMoney(String money)
	{	
		DecimalFormat df = new DecimalFormat("#,###.00");
		return money=df.format(new BigDecimal(money));	
	}

	public boolean checkEmailFormat(String email)
	{	
		CheckEmailFormating checkEmail=new CheckEmailFormating(); 
		if(checkEmail.checkEmailValid(email))
			return true;//email valid
		else
			return false;//email invalid	
	}
	

	private void addEvents() {
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int ret=JOptionPane.showConfirmDialog(null, 
						"Bạn có chắc chắn muốn thoát Quản lý tài khoản", 
						"Thông báo thoát", 
						JOptionPane.YES_NO_OPTION);
				if(ret==JOptionPane.NO_OPTION)return;
				dispose();
			}
		});

		btnAddStock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				addStockHandling();
			}
		});

		btnUpdateStock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateStockHandling();
				updateAccount();
			}
		});

		btnDeleteStock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteStockHandling();
				updateAccount();
			}
		});

		btnEditStock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				editStockHandling();
				updateAccount();
			}
		});

		btnAddAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addAccountHandling();
			}
		});
		
		btnUpdateAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
					updateAccountHandling();
					updateAccount();
			}
		});

		btnDeleteAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteAccountHandling();
			}
		});

		btnEditAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				editAccountHandling();
			}
		});

		tblStock.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				int rowTableStock=tblStock.getSelectedRow();
				if(rowTableStock==-1)return;

				String tradingStockDay=tblStock.getValueAt(rowTableStock, 1)+"";
				String stockCode=tblStock.getValueAt(rowTableStock, 2)+"";
				String price=tblStock.getValueAt(rowTableStock, 3)+"";
				String quantity=tblStock.getValueAt(rowTableStock, 4)+"";

				txtStartDay.setText(tradingStockDay);
				txtStockCode.setText(stockCode);
				txtPrice.setText(price);
				txtQuantity.setText(quantity);

			}
		});

		listAccount.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				if(listAccount.getSelectedValue()==null)return;

				lstStock=hienThiChiTietCKTheoSoTK(
						listAccount.getSelectedValue().getSoTK(),1);
				dtmStock.setRowCount(0);
				int id=1;//index 1,2,3,...
				float totalMoney=0;//calculate total money
				for(Stock ck:lstStock)
				{
					Vector<Object> vec=new Vector<Object>();

					vec.add(id);
					id++;
					
					vec.add(ck.getNgay());
					txtStartDay.setText(ck.getNgay());
					vec.add(ck.getMaVaChiSoCK());
					txtStockCode.setText(ck.getMaVaChiSoCK());
					vec.add(ck.getGia());
					txtPrice.setText(String.valueOf(ck.getGia()));
					vec.add(ck.getSoLuong());
					txtQuantity.setText(String.valueOf(ck.getSoLuong()));

					float price=ck.getGia();
					float quantity=ck.getSoLuong();
					float money=price*quantity;
					vec.add(money);

					totalMoney+=money;
					dtmStock.addRow(vec);		
				}

				txtTotalStockValue.setText(String.valueOf(totalMoney));

				ArrayList<Account>lstAccount=
						hienThiChiTietTKTheoSoTK(
								listAccount.getSelectedValue().getSoTK());
				String selectedValueSoTK=listAccount.getSelectedValue().getSoTK();

				for(Account tk:lstAccount)
				{	
					txtAccountNumber.setText(tk.getSoTK());
					txtAccountName.setText(tk.getTenTK());
					txtYOB.setText(tk.getNamSinh());
					txtMobile.setText(tk.getSoDT());
					txtIdentityCardNumber.setText(tk.getSoCMND());
					txtEmail.setText(tk.getEmail());
					txtAddress.setText(tk.getDiaChi());
					txtCashBalance.setText(String.valueOf(tk.getSoDuTienMatBD()));
					txtTotalAssets.setText(String.valueOf(tk.getTongTaiSanBD()));

				}
				updateAccount();
			}
		});

	}		

	protected void updateAccount() {
		try {
			String sqlCapNhatTK="update TaiKhoan set SoDuTienMatBD=?,TongTaiSanBD=? where SoTK=?";
			preStatement=conn.prepareStatement(sqlCapNhatTK);
			
			float cashBalance=Float.parseFloat(txtCashBalance.getText());
			preStatement.setFloat(1, cashBalance);
			
			float totalStockValue=Float.parseFloat(txtTotalStockValue.getText());
			float totalAssets=cashBalance+totalStockValue;
			
			//totalAssets=totalStockValue+cashBalance;
			preStatement.setFloat(2, totalAssets);
			
			preStatement.setString(3, txtAccountNumber.getText());
			preStatement.executeUpdate();
				
		} catch (SQLException e) {
			e.printStackTrace();		
		}catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, 
					"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
					+ "Cập nhật tài khoản thất bại!");		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	protected void editAccountHandling() {
		openInputData();
		txtStockCode.setEditable(false);
		txtPrice.setEditable(false);
		txtQuantity.setEditable(false);

		try {
			//if 1: If the account name does not contain numerical characters, 
			//then check the account number
			if(!checkStringHasLeast1Number(txtAccountName.getText()))
			{
				//if 2: Check the account number is not the account, 
				//edit this account user entered correctly
				if(checkAccountNumberInAccountTableExist(txtAccountNumber.getText())
						&& checkStringHasNumber(txtAccountNumber.getText())
						&& (txtAccountNumber.getText().toString().length()==12))
				{		
					//if 3: check format dd/MM/yyyy of YOB
					if(checkDayMonthYearFormat(txtYOB.getText()))
					{	
						//if 4: If the number of identity card number (already exists) 
						//or the correct format is correct, then check the phone number
						if(checkIdentityCardNumberInAccountTableExist(txtIdentityCardNumber.getText())
								&& checkStringHasNumber(txtIdentityCardNumber.getText())
								&& (txtIdentityCardNumber.getText().toString().length()>8)
								&& (txtIdentityCardNumber.getText().toString().length()<13))
						{		
							//if 5: If the phone number (already exists) and 
							//the entry for correct formatting, check email
							if(checkMobileInAccountTableExist(txtMobile.getText())
									&& checkStringHasNumber(txtMobile.getText())
									&& (txtMobile.getText().toString().length()==10))
							{		
								//if 6: If the email (already exists) and the entry for 
								//correct formatting then check for cash balance, total assets
								if(checkEmailInAccountTableExist(txtEmail.getText())
										&& checkEmailFormat(txtEmail.getText()))
								{
									//if 7: check condition for cash balance:
									// - no negative number, no more than 2 trillion, 
									// - does not contain special characters, 
									// - you must manually enter all 2 of this
									if((Integer.parseInt(txtCashBalance.getText())>=0) 
											&& (Integer.parseInt(txtCashBalance.getText())<2000000000)
											&&(checkStringHasNumber(txtCashBalance.getText())) )
									{
										implementEditAccountHandling();//edit account
									}
									else
									{
										JOptionPane.showMessageDialog(null, 
												"Số dư tiền mặt["+txtCashBalance.getText()+"] "
														+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
														+ "- Không được bỏ trống 2 mục này;\n"
														+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
														+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
														+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
									}				
								}
								else//else 6: If the email (already exists) of the account 
									//(already exists) then the account update notification failed
								{
									JOptionPane.showMessageDialog(null, 
											"Email["+txtEmail.getText()+"] chưa tồn tại "
													+ "hoặc chưa đúng định dạng email (vd: abc123@gmail.com). "
													+ "Sửa tài khoản thất bại!");
								}	
							}
							else//else 5: If the phone number (already exists) in the account number 
								//(already exists), the account update notification failed
							{
								JOptionPane.showMessageDialog(null, 
										"Số DT["+txtMobile.getText()+"] chưa tồn tại "
												+ "hoặc có chứa ký tự chữ "
												+ "hoặc chưa đủ/vượt quá 10 số. "
												+ "Sửa tài khoản thất bại!");
							}
						}
						else//else 4: If the identity card number (already exists) in the account 
							//(already exists), the account update notification failed
						{
							JOptionPane.showMessageDialog(null, 
									"Số CMND["+txtIdentityCardNumber.getText()+"] chưa tồn tại "
											+ "hoặc có chứa ký tự chữ "
											+ "hoặc chưa đủ/vượt quá khoảng [9->12] số. "
											+ "Sửa tài khoản thất bại!");	
						}	

					}	
					else//else 3: If the wrong dd/MM/yyyy format failed, 
						//then the account update notification failed
					{
						JOptionPane.showMessageDialog(null, 
								"Năm sinh["+txtYOB.getText()+"] sai định dạng dd/MM/yyyy. "
										+ "Sửa tài khoản thất bại!");
					}		
				}
				else//else 2: If the account number (already exists) 
					//then the account update notification failed
				{
					JOptionPane.showMessageDialog(null, 
							"Số TK["+txtAccountNumber.getText()+"] chưa tồn tại "
									+ "hoặc có chứa ký tự chữ "
									+ "hoặc chưa đủ/vượt quá 12 ký tự. "
									+ "Sửa tài khoản thất bại!");
				}
			}
			else//else 1: If the account name contains numeric characters, 
				//the account update notification fails
			{
				JOptionPane.showMessageDialog(null, 
						"Tên TK["+txtAccountName.getText()+"] chứa ký tự số là không hợp lệ "
								+ "hoặc không tự nhập tay lại mục cần sửa. "
								+ "Sửa tài khoản thất bại!");
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, 
					"Số dư tiền mặt["+txtCashBalance.getText()+"] "
							+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
							+ "- Không được bỏ trống 2 mục này;\n"
							+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
							+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
							+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
		} catch (HeadlessException e) {
			e.printStackTrace();
		}


	}

	private void implementEditAccountHandling() {
		int ret=JOptionPane.showConfirmDialog(null, 
				"Số TK["+txtAccountNumber.getText()+"] đã tồn tại và các thông tin chỉnh sửa đã chính xác.\n"
						+ "Bạn có chắc chắn muốn sửa tài khoản?", 
						"Sửa tài khoản", 
						JOptionPane.YES_NO_OPTION);
		if(ret==JOptionPane.NO_OPTION)return;
		try
		{
			//Edit information from TaiKhoan table
			String sqlTableTKBD="update TaiKhoan set TenTK=?,NamSinh=?,SoCMND=?,DiaChi=?,SoDT=?,Email=?,SoDuTienMatBD=?,TongTaiSanBD=? where SoTK=?";
			preStatement=conn.prepareStatement(sqlTableTKBD);

			preStatement.setString(1, txtAccountName.getText());
			preStatement.setString(2, txtYOB.getText());
			preStatement.setString(3, txtIdentityCardNumber.getText());
			preStatement.setString(4, txtAddress.getText());
			preStatement.setString(5, txtMobile.getText());
			preStatement.setString(6, txtEmail.getText());
			preStatement.setInt(7, Integer.parseInt(txtCashBalance.getText()));
			
			float totalStockValue=Float.parseFloat(txtTotalStockValue.getText());
			float cashBalance=Integer.parseInt(txtCashBalance.getText());
			float totalAssets=totalStockValue+cashBalance;
			preStatement.setFloat(8, totalAssets);

			preStatement.setString(9, txtAccountNumber.getText());

			int xTableTKBD=preStatement.executeUpdate();
			if(xTableTKBD>0)
			{
				displayStockAfterUpdateStock();
				closeInputData();
			}		
			
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, 
					"Số dư tiền mặt ["+txtCashBalance.getText()+"]"
							+ "hoặc Tổng tài sản ["+txtTotalAssets.getText()+"] "
							+ "chứa ký tự số là không hợp lệ. "
							+ "Cập nhật tài khoản thất bại!");
		}			
		catch(Exception e)
		{
			e.printStackTrace();
		}			
	}

	protected void deleteAccountHandling() {
		openInputData();
		//If the account number (already exists) then delete it
		if(checkAccountNumberInAccountTableExist(txtAccountNumber.getText()))
		{
			int ret=JOptionPane.showConfirmDialog(null, 
					"Số TK["+txtAccountNumber.getText()+"] đã tồn tại.\n"
							+ "Bạn có chắc chắn muốn xóa tài khoản?", 
							"Xóa tài khoản", 
							JOptionPane.YES_NO_OPTION);
			if(ret==JOptionPane.NO_OPTION)return;
			try
			{
				//Deleted account and stocks on ChungKhoan table			
				String sqlTableCKBD="delete from ChungKhoan where SoTK=(select SoTK from TaiKhoan where SoTK=?)";
				preStatement=conn.prepareStatement(sqlTableCKBD);
				preStatement.setString(1, txtAccountNumber.getText());
				int xTableCKBD=preStatement.executeUpdate();
				if(xTableCKBD>0)
				{
					txtStartDay.setText("");
					txtStockCode.setText("");
					txtPrice.setText("");
					txtQuantity.setText("");
					txtTotalStockValue.setText("");
					displayStockAfterUpdateStock();
					closeInputData();
				}	

				//Deleted account and stocks on TaiKhoan table
				String sqlBangTKBD="delete from TaiKhoan where SoTK=?";
				preStatement=conn.prepareStatement(sqlBangTKBD);
				preStatement.setString(1, txtAccountNumber.getText());

				int xBangTKBD=preStatement.executeUpdate();
				if(xBangTKBD>0)
				{	
					txtStockCode.setText("");
					txtPrice.setText("");
					txtQuantity.setText("");
					txtTotalStockValue.setText("");
					displayStockAfterUpdateStock();
					closeInputData();
				}	
				displayListOfAccountNumber();
				
					
				//Deleted account on TienNapRut table
				String sqlBangTNR="delete from TienNapRut where SoTK=(select SoTK from TaiKhoan where SoTK=?)";
				preStatement=conn.prepareStatement(sqlBangTNR);
				preStatement.setString(1, txtAccountNumber.getText());
				int xBangTNR=preStatement.executeUpdate();
				if(xBangTNR>0)
				{
					txtStartDay.setText("");
					txtStockCode.setText("");
					txtPrice.setText("");
					txtQuantity.setText("");
					txtTotalStockValue.setText("");
					displayStockAfterUpdateStock();
					closeInputData();
				}	

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}			
		}
		else//If the account number (does not yet exist) 
			//then the account deletion message failed
		{
			JOptionPane.showMessageDialog(null, 
					"Số TK["+txtAccountNumber.getText()+"] không tồn tại. "
							+ "Xóa tài khoản thất bại!");
		}
		
	}
	
	protected void updateAccountHandling()
	{
		try {
			//if 1: If the account name does not contain numerical characters and is not blank, 
			//then check the account number
			if( !checkStringHasLeast1Number(txtAccountName.getText()) )
			{
				try {
					//if 2: If the account number (not yet) and not empty, 
					//then check the identity card number
					if( !checkAccountNumberInAccountTableExist(txtAccountNumber.getText())
							&& checkStringHasNumber(txtAccountNumber.getText())
							&& (txtAccountNumber.getText().toString().length()==12) )
					{		
						try {
							//if 3: check format dd/MM/yyyy of YOB and no empty
							if( checkDayMonthYearFormat(txtYOB.getText()) )
							{	
								try {
									//if 4: if identity card number(not already exists) and no empty so check mobile next
									if( !checkIdentityCardNumberInAccountTableExist(txtIdentityCardNumber.getText())
											&& checkStringHasNumber(txtIdentityCardNumber.getText())
											&& (txtIdentityCardNumber.getText().toString().length()>8)
											&& (txtIdentityCardNumber.getText().toString().length()<13) )
									{		
										try {
											//if 5: if mobile(not already exists) and no empty so check email next
											if( !checkMobileInAccountTableExist(txtMobile.getText())
													&& checkStringHasNumber(txtMobile.getText())
													&& (txtMobile.getText().toString().length()==10) )
											{		
												try {
													//if 6: if email(not already exists) and no empty so implement update new account
													if( !checkEmailInAccountTableExist(txtEmail.getText())
															&& checkEmailFormat(txtEmail.getText()) )
													{
														try {
															//if 7: check condition for total initial withdrawal
															// - No negative number, no more than 2 trillion,
															// - Does not contain special characters,
															// - Must manually enter this item
															if( (Integer.parseInt(txtCashBalance.getText())>=0) 
																	&& (Integer.parseInt(txtCashBalance.getText())<2000000000)
																	&& (checkStringHasNumber(txtCashBalance.getText())) )
															{
																thucHienCapNhatTK();//update account
															}
															else
															{
																JOptionPane.showMessageDialog(null, 
																		"Số dư tiền mặt["+txtCashBalance.getText()+"] "
																				+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
																				+ "- Không được bỏ trống, nên tự nhập tay;\n"
																				+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
																				+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n");
															}
														}catch(NumberFormatException e){
															JOptionPane.showMessageDialog(null, 
																	"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
																	+ "Cập nhật tài khoản thất bại!");
														} catch (Exception e) {
															e.printStackTrace();
														}				
													}
													else//else 6: If the email (already exists) in the account (already exists) 
														//then the account update notification failed
													{
														JOptionPane.showMessageDialog(null, 
																"Email["+txtEmail.getText()+"] đã tồn tại "
																		+ "hoặc bị bỏ trống, "
																		+ "hoặc chưa đúng định dạng email (vd: abc123@gmail.com). "
																		+ "Cập nhật tài khoản thất bại!");
													}
												}catch(NumberFormatException e){
													JOptionPane.showMessageDialog(null, 
															"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
															+ "Cập nhật tài khoản thất bại!");
												} catch (Exception e) {
													e.printStackTrace();
												}	
											}
											else//else 5: nếu số đt(đã tồn tại) trong số tk(đã tồn tại) thì thông báo cập nhật tk thất bại
											{
												JOptionPane.showMessageDialog(null, 
														"Số DT["+txtMobile.getText()+"] đã tồn tại "
																+ "hoặc bị bỏ trống, "
																+ "hoặc có chứa ký tự chữ, "
																+ "hoặc chưa đủ/vượt quá 10 số. "
																+ "Cập nhật tài khoản thất bại!");
											}
										}catch(NumberFormatException e){
											JOptionPane.showMessageDialog(null, 
													"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
													+ "Cập nhật tài khoản thất bại!");
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									else//else 4: If mobile (already exists) in the account (already exists), 
										//the account update notification failed
									{
										JOptionPane.showMessageDialog(null, 
												"Số CMND["+txtIdentityCardNumber.getText()+"] đã tồn tại "
														+ "hoặc bị bỏ trống, "
														+ "hoặc có chứa ký tự chữ, "
														+ "hoặc chưa đủ/vượt quá khoảng [9->12] số. "
														+ "Cập nhật tài khoản thất bại!");	
									}
								}catch(NumberFormatException e){
									JOptionPane.showMessageDialog(null, 
											"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
											+ "Cập nhật tài khoản thất bại!");
								} catch (Exception e) {
									e.printStackTrace();
								}	

							}	
							else//else 3: If the wrong dd/MM/yyyy format failed, 
								//then the account update notification failed
							{
								JOptionPane.showMessageDialog(null, 
										"Năm sinh["+txtYOB.getText()+"] sai định dạng dd/MM/yyyy "
												+ "hoặc bị bỏ trống. "
												+ "Cập nhật tài khoản thất bại!");
							}
						}catch(NumberFormatException e){
							JOptionPane.showMessageDialog(null, 
									"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
									+ "Cập nhật tài khoản thất bại!");
						} catch (Exception e) {
							e.printStackTrace();
						}		
					}
					else//else 2: if account number(already exists) so notify update account fails
					{
						JOptionPane.showMessageDialog(null, 
								"Số TK["+txtAccountNumber.getText()+"] đã tồn tại "
										+ "hoặc bị bỏ trống, "
										+ "hoặc có chứa ký tự chữ, "
										+ "hoặc chưa đủ/vượt quá 12 ký tự. "
										+ "Cập nhật tài khoản thất bại!");
					}
				}catch(NumberFormatException e){
					JOptionPane.showMessageDialog(null, 
							"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
							+ "Cập nhật tài khoản thất bại!");
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}
			else//else 1: if account name have any number characters so notify update account fails
			{
				JOptionPane.showMessageDialog(null, 
						"Tên TK["+txtAccountName.getText()+"] chứa ký tự số là không hợp lệ "
								+ "hoặc bị bỏ trống, "
								+ "Cập nhật tài khoản thất bại!");
			}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, 
					"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
					+ "Cập nhật tài khoản thất bại!");
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
		
		

	}

	private void thucHienCapNhatTK() {
		
		try {
			float totalStockValue=Float.parseFloat(txtTotalStockValue.getText());//initial totalStockValue to calculate totalAssets
			float cashBalance=Float.parseFloat(txtCashBalance.getText());//initial cash balance
			float totalAssets=cashBalance+totalStockValue;//initial totalAssets
			
			int ret=JOptionPane.showConfirmDialog(null, 
					"Số TK["+txtAccountNumber.getText()+"] chưa tồn tại.\n"
							+ "Bạn có chắc chắn muốn cập nhật tài khoản?", 
							"Cập nhật tài khoản", 
							JOptionPane.YES_NO_OPTION);
			if(ret==JOptionPane.NO_OPTION)return;
			try
			{
				//update new account into TaiKhoan table
				String sqlBangTK="insert into TaiKhoan values(?,?,?,?,?,?,?,?,?)";
				preStatement=conn.prepareStatement(sqlBangTK);
				preStatement.setString(1, txtAccountNumber.getText());
				preStatement.setString(2, txtAccountName.getText());
				preStatement.setString(3, txtYOB.getText());
				preStatement.setString(4, txtMobile.getText());
				preStatement.setString(5, txtIdentityCardNumber.getText());
				preStatement.setString(6, txtEmail.getText());
				preStatement.setString(7, txtAddress.getText());
				preStatement.setFloat(8, cashBalance);//note cash balance
				
				totalStockValue=0;//totalStockValueBD=0 -> not yet buy any stock
				totalAssets=cashBalance+totalStockValue;
				preStatement.setFloat(9, totalAssets);//totalAssets

				int xBangTK=preStatement.executeUpdate();
				if(xBangTK>0)
				{
					displayListOfAccountNumber();
					closeInputData();
				}

			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, 
						"Số dư tiền mặt ["+txtCashBalance.getText()+"], "
								+ "Ngày bắt đầu ["+txtStartDay.getText()+"], "
								+ "Giá["+txtPrice.getText()+"], "
								+ "Số lượng["+txtQuantity.getText()+"] "
								+ "không hợp lệ. "
								+ "Cập nhật tài khoản thất bại!");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, 
					"Chưa nhập đủ hoặc nhập sai thông tin, vui lòng kiểm tra lại. "
					+ "Cập nhật tài khoản thất bại!");
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
		
		
	}

	protected void addAccountHandling() {
		openInputData();
		txtAccountName.setText("");
		txtYOB.setText("");
		txtIdentityCardNumber.setText("");
		txtAddress.setText("");
		txtMobile.setText("");
		txtEmail.setText("");
		txtStartDay.setText("");
		txtAccountNumber.setText("");
		txtCashBalance.setText("0");
		txtTotalAssets.setText("");
		txtStockCode.setText("");
		txtStockCode.setEditable(false);
		txtPrice.setText("");
		txtPrice.setEditable(false);
		txtQuantity.setText("");
		txtQuantity.setEditable(false);
		txtTotalStockValue.setText("");
	}

	protected void editStockHandling() {
		openInputData();
		//If the account number (already exists), then check the repair
		if(checkAccountNumberInStockTableExist(txtAccountNumber.getText()))
		{
			try {
				//if 2: Check the stock number, price, quantity
				if((!checkStringHasNumber(txtStockCode.getText())) 
						&& (txtStockCode.getText().toString().length()==3)  
						&& (checkPriceFormat(txtPrice.getText()))   
						&& (checkQuantityFormat(txtQuantity.getText()))
						&& (!checkDayMonthYearFormat(txtStartDay.getText())) )
				{
					inplementEditStockHandling();//edit stock
				}
				else
				{
					JOptionPane.showMessageDialog(null, 
							"Ngày bắt đầu["+txtStartDay.getText()+"], "
									+ "Mã CK["+txtStockCode.getText()+"], "
									+ "Giá["+txtPrice.getText()+"], "
									+ "Số lượng["+txtQuantity.getText()+"] "
									+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
									+ "- Ngày mua/bán CK phải đúng định dạng (dd/MM/yyyy);\n"
									+ "- Mã CK không được vượt quá 3 ký tự số;\n"
									+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
									+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
									+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
									+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
				}
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null,
						"Ngày bắt đầu["+txtStartDay.getText()+"], "
								+ "Giá["+txtPrice.getText()+"], "
								+ "hoặc Số lượng["+txtQuantity.getText()+"] "
								+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
								+ "- Ngày mua/bán CK phải đúng định dạng (dd/MM/yyyy);\n"
								+ "- Mã CK không được vượt quá 3 ký tự số;\n"
								+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
								+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
								+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
								+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}
		else//if account number(not already exists) so notify check edit next
		{
			JOptionPane.showMessageDialog(null, 
					"Số TK["+txtAccountNumber.getText()+"] không tồn tại. "
							+ "Sửa chứng khoán thất bại!");
		}	

	}

	private void inplementEditStockHandling() {
		int ret=JOptionPane.showConfirmDialog(null, 
				"Mã CK["+txtStockCode.getText()+"] "
						+ "trong Số TK["+txtAccountNumber.getText()+"] đã tồn tại.\n"
						+ "Bạn có chắc chắn muốn sửa chứng khoán?", 
						"Sửa chứng khoán", 
						JOptionPane.YES_NO_OPTION);
		if(ret==JOptionPane.NO_OPTION)return;
		try
		{
			//edit into ChungKhoan table
			String sql="update ChungKhoan set Ngay=?,Gia=?,SoLuong=?,ThanhTien=? where SoTK=? and MaVaChiSoCK=?";
			preStatement=conn.prepareStatement(sql);

			preStatement.setString(1, txtStartDay.getText());
			preStatement.setFloat(2, Float.parseFloat(txtPrice.getText()));
			preStatement.setInt(3, Integer.parseInt(txtQuantity.getText()));

			float gia=Float.parseFloat(txtPrice.getText());
			int soLuong=Integer.parseInt(txtQuantity.getText());
			float thanhTien=gia*soLuong;
			preStatement.setFloat(4, thanhTien);

			preStatement.setString(5, txtAccountNumber.getText());
			preStatement.setString(6, txtStockCode.getText());

			int x=preStatement.executeUpdate();
			if(x>0)
			{
				displayStockAfterUpdateStock();
				closeInputData();
			}		
			
		}
		catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, 
					"Mã CK["+txtStockCode.getText()+"], "
							+ "Ngày bắt đầu["+txtStartDay.getText()+"], "
							+ "Giá["+txtPrice.getText()+"], "
							+ "Số lượng["+txtQuantity.getText()+"] "
							+ "không tồn tại hoặc nhập sai dữ liệu. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
							+ "- Ngày mua/bán CK phải có định dạng (dd/MM/yyyy);\n"
							+ "- Mã CK không được lớn hơn 3 ký tự;\n"
							+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
							+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
							+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
							+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	protected void deleteStockHandling() {
		openInputData();
		try {
			//if account number(already exists) so continue to check delete function
			if(checkAccountNumberInStockTableExist(txtAccountNumber.getText()))
			{
				if(!checkStockCodeByAccountNumberInStockTableExist2(txtAccountNumber.getText(),txtStockCode.getText()))
				{
					//if stock code(not already exists) in account number(already exists) 
					//so prevent to delete stock function
					JOptionPane.showMessageDialog(null, 
							"Mã CK["+txtStockCode.getText()+"] "
									+ "trong Số TK["+txtAccountNumber.getText()+"] không tồn tại. "
									+ "Xóa chứng khoán thất bại!");
				}
				else//if stock code(already exists) into account number(already exists) 
					//so allow to delete stock				
				{
					int ret=JOptionPane.showConfirmDialog(null, 
							"Mã CK["+txtStockCode.getText()+"] "
									+ "trong Số TK["+txtAccountNumber.getText()+"] đã tồn tại.\n"
									+ "Bạn có chắc chắn muốn xóa chứng khoán?", 
									"Xóa chứng khoán", 
									JOptionPane.YES_NO_OPTION);
					if(ret==JOptionPane.NO_OPTION)return;
					try
					{
						//delete into ChungKhoan table
						String sql="delete from ChungKhoan where SoTK=? and MaVaChiSoCK=?";
						preStatement=conn.prepareStatement(sql);
						preStatement.setString(1, txtAccountNumber.getText());
						preStatement.setString(2, txtStockCode.getText());

						int x=preStatement.executeUpdate();
						if(x>0)
						{
							txtStartDay.setText("");
							txtStockCode.setText("");
							txtPrice.setText("");
							txtQuantity.setText("");
							txtTotalStockValue.setText("");
							displayStockAfterUpdateStock();
							closeInputData();
						}	
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}	
				}		
			}
			else//if account number(not already exist) 
				//so prevent continue to check delete function
			{
				JOptionPane.showMessageDialog(null, 
						"Số TK["+txtAccountNumber.getText()+"] không tồn tại. "
								+ "Xóa chứng khoán thất bại!");
			}
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, 
					"Mã CK["+txtStockCode.getText()+"], "
							+ "Ngày bắt đầu["+txtStartDay.getText()+"], "
							+ "Giá["+txtPrice.getText()+"], "
							+ "Số lượng["+txtQuantity.getText()+"] "
							+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
							+ "- Ngày bắt đầu phải có định dạng (dd/MM/yyyy);\n"
							+ "- Mã CK không được lớn hơn 3 ký tự;\n"
							+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
							+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
							+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
							+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
		} catch (HeadlessException e) {
			e.printStackTrace();
		}

	}

	protected void updateStockHandling() {
		//if account number(already exists) so allow to check adding function
		if(checkAccountNumberInStockTableExist(txtAccountNumber.getText()))
		{
			if(checkStockCodeByAccountNumberInStockTableExist(txtAccountNumber.getText(), txtStockCode.getText()))
			{
				//If the stock code (already exists) in account (already exists) 
				//or violates the condition in the if not add new
				JOptionPane.showMessageDialog(null, 
						"Mã CK["+txtStockCode.getText()+"] "
								+ "trong Số TK["+txtAccountNumber.getText()+"] đã tồn tại. "
								+ "Cập nhật chứng khoán thất bại!");
			}
			else//If the stock code (not yet available) of the account
				//(already exists) then add new stock
			{
//				try {
//					if( (!checkStringHasNumber(txtStockCode.getText())) 
//							&& (txtStockCode.getText().toString().length()==3)  
//							&& (checkPriceFormat(txtPrice.getText()))   
//							&& (checkQuantityFormat(txtQuantity.getText())) )
//					{
//						implementUpdateStockHandling();//update stock
//					}
//					else
//					{
//						JOptionPane.showMessageDialog(null, 
//								"Mã CK["+txtStockCode.getText()+"], "
//										+ "Ngày bắt đầu["+txtStartDay.getText()+"], "
//										+ "Giá["+txtPrice.getText()+"], "
//										+ "Số lượng["+txtQuantity.getText()+"] "
//										+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
//										+ "- Ngày bắt đầu phải có định dạng (dd/MM/yyyy);\n"
//										+ "- Mã CK không được lớn hơn 3 ký tự;\n"
//										+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
//										+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
//										+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
//										+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
//					}
//				}catch(NumberFormatException e){
//					JOptionPane.showMessageDialog(null, 
//							"Mã CK["+txtStockCode.getText()+"], "
//									+ "Ngày bắt đầu["+txtStartDay.getText()+"], "
//									+ "Giá["+txtPrice.getText()+"], "
//									+ "Số lượng["+txtQuantity.getText()+"] "
//									+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
//									+ "- Ngày bắt đầu phải có định dạng (dd/MM/yyyy);\n"
//									+ "- Mã CK không được lớn hơn 3 ký tự;\n"
//									+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
//									+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
//									+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
//									+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
//				} catch (HeadlessException e) {
//					e.printStackTrace();
//				}
				
				//Cố tình để lại lỗi để dùng tool test tìm lỗi
				//Deliberately leaving error to using testing tool to find error
				
				if( (!checkStringHasNumber(txtStockCode.getText())) 
						&& (txtStockCode.getText().toString().length()==3)  
						//&& (checkPriceFormat(txtPrice.getText()))  -> crash at here if have wrong input
						&& (checkQuantityFormat(txtQuantity.getText())) )
				{
					implementUpdateStockHandling();//update stock
				}

			}		
		}
		else//If the account number (does not exist yet) is not checked for new additions
		{
			implementUpdateStockHandling();//update stock
		}

	}

	private void implementUpdateStockHandling() {
		float totalStockValue=0;
		int ret=JOptionPane.showConfirmDialog(null, 
				"Mã CK["+txtStockCode.getText()+"] "
						+ "trong Số TK["+txtAccountNumber.getText()+"] chưa tồn tại.\n"
						+ "Bạn có chắc chắn muốn cập nhật chứng khoán?", 
						"Cập nhật chứng khoán", 
						JOptionPane.YES_NO_OPTION);
		if(ret==JOptionPane.NO_OPTION)return;
		try
		{
			//update new stock into ChungKhoan table
			String sqlBangCK="insert into ChungKhoan(SoTK,Ngay,LoaiNgay,MaVaChiSoCK,LoaiCKHayCS,Gia,SoLuong,ThanhTien,Lenh) "
					+ "values((select SoTK from TaiKhoan where SoTK=?),?,?,?,?,?,?,?,?)";
			preStatement=conn.prepareStatement(sqlBangCK);

			preStatement.setString(1, txtAccountNumber.getText());
			preStatement.setString(2, txtStartDay.getText());//start day
			preStatement.setInt(3, 0);//LoaiNgay=0 -> start day
			preStatement.setString(4, txtStockCode.getText());//MaVaChiSoCK
			preStatement.setInt(5, 0);//LoaiCKHayCS=0 -> stock
			preStatement.setFloat(6, Float.parseFloat(txtPrice.getText()));	
			preStatement.setInt(7, Integer.parseInt(txtQuantity.getText()));

			float gia=Float.parseFloat(txtPrice.getText());//price
			float soLuong=Float.parseFloat(txtQuantity.getText());//quantity
			float thanhTien=gia*soLuong;
			preStatement.setFloat(8, thanhTien);//count money
			totalStockValue+=thanhTien;//Calculate the total value of securities to calculate total assets
			
			preStatement.setString(9, "BD");//Lenh="BD"->add stock at start day
			
			int xBangCK=preStatement.executeUpdate();
			if(xBangCK>0)
			{
				displayStockAfterUpdateStock();
				txtTotalStockValue.setText(String.valueOf(totalStockValue));
				closeInputData();
			}
//			
//		}catch(NumberFormatException e){
//			JOptionPane.showMessageDialog(null, 
//					"Mã CK["+txtStockCode.getText()+"], "
//							+ "Ngày bắt đầu["+txtStartDay.getText()+"], "
//							+ "Giá["+txtPrice.getText()+"], "
//							+ "Số lượng["+txtQuantity.getText()+"] "
//							+ "nhập sai. Vui lòng kiểm tra lại 1 số điều kiện sau:\n"
//							+ "- Ngày bắt đầu phải có định dạng (dd/MM/yyyy);\n"
//							+ "- Mã CK không được lớn hơn 3 ký tự;\n"
//							+ "- Không được để trống, giá có thể là số thực, số lượng phải là số nguyên;\n"
//							+ "- Không được chứa số âm và không vượt quá 2 tỷ tỷ (2000000000);\n"
//							+ "- Không được chứa ký tự đặc biệt (vd: abc,+-*/=\\?~@#$%^&<>[]{}...);\n"
//							+ "- Không tự nhập tay toàn bộ vào 2 mục này.");
//		}
			//Cố tình để lại lỗi để dùng tool test tìm lỗi
			//Deliberately leaving error to using testing tool to find error
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void addStockHandling() {
		txtStockCode.setEditable(true);
		txtPrice.setEditable(true);
		txtQuantity.setEditable(true);
		txtStartDay.setEditable(true);
		txtStockCode.setText("");
		txtPrice.setText("");
		txtQuantity.setText("");
		txtTotalStockValue.setText("");
	}

	private void closeInputData() {	
		txtAccountName.setEditable(false);
		txtYOB.setEditable(false);
		txtMobile.setEditable(false);
		txtIdentityCardNumber.setEditable(false);
		txtAccountNumber.setEditable(false);
		txtEmail.setEditable(false);
		txtAddress.setEditable(false);
		txtStartDay.setEditable(false);
		
		txtStockCode.setEditable(false);
		txtPrice.setEditable(false);
		txtQuantity.setEditable(false);
				
		txtCashBalance.setEditable(false);
		txtTotalAssets.setEditable(false);
		
		tblStock.setEnabled(true);
		txtTotalStockValue.setEditable(false);
		
	}

	private void openInputData() {
		txtAccountName.setEditable(true);
		txtYOB.setEditable(true);
		txtMobile.setEditable(true);
		txtIdentityCardNumber.setEditable(true);
		txtAccountNumber.setEditable(true);
		txtEmail.setEditable(true);
		txtAddress.setEditable(true);
		txtStartDay.setEditable(true);
		
		txtStockCode.setEditable(true);
		txtPrice.setEditable(true);
		txtQuantity.setEditable(true);
				
		txtCashBalance.setEditable(true);
		txtTotalAssets.setEditable(false);
		
		tblStock.setEnabled(true);
		txtTotalStockValue.setEditable(false);
	}

/*------------------- Finish processing the data from the database ------------------*/
	
	
/*------------------- Start dealing with the interface ------------------*/
	
	//This function is used to capture the list details of stock by account number 
	//when click event in the addEvents() function.
	public ArrayList<Stock> hienThiChiTietCKTheoSoTK(String soTK, int loai) {
		ArrayList<Stock>lstStock=new ArrayList<Stock>();
		try {
			String sql="select * from ChungKhoan where SoTK=? and LoaiCKHayCS=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, soTK);
			preStatement.setInt(2, 0);//0 -> type is stock
	
			result=preStatement.executeQuery();
			
			while(result.next())
			{
				Stock ck=new Stock();
				ck.setSoTK(result.getString(1));
				ck.setSttCK(result.getInt(2));
				ck.setNgay(result.getString(3));
				ck.setLoaiNgay(result.getInt(4));
				ck.setMaVaChiSoCK(result.getString(5));
				ck.setLoaiCKHayCS(result.getInt(6));
				ck.setGia(result.getFloat(7));
				ck.setSoLuong(result.getInt(8));				
				ck.setThanhTien(result.getFloat(9));			
				ck.setLenh(result.getString(10));
				lstStock.add(ck);		
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstStock;
	}

	//This function is used to capture the list details of account by account number 
	//when click event in the addEvents() function.
	public ArrayList<Account> hienThiChiTietTKTheoSoTK(String soTK) {
		ArrayList<Account>lstAccount=new ArrayList<Account>();
		try {
			String sqlChiTietTK="select * from TaiKhoan where SoTK=?";
			preStatement=conn.prepareStatement(sqlChiTietTK);
			preStatement.setString(1, soTK);
	
			result=preStatement.executeQuery();
			while(result.next())
			{
				Account tk=new Account();
				tk.setSoTK(result.getString(1));
				tk.setTenTK(result.getString(2));	
				tk.setNamSinh(result.getString(3));	
				tk.setSoDT(result.getString(4));
				tk.setSoCMND(result.getString(5));
				tk.setEmail(result.getString(6));
				tk.setDiaChi(result.getString(7));
				tk.setSoDuTienMatBD(result.getInt(8));
				tk.setTongTaiSanBD(result.getFloat(9));
	
				lstAccount.add(tk);		
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstAccount;
	}
	
	public void displayAllOfStock() 
	{
		float totalStockValue = 0;
		for (int i = 0; i < tblStock.getRowCount(); i++){
			float giaTriCK = Float.parseFloat((String) tblStock.getValueAt(i, 5));
			totalStockValue += giaTriCK;
		}
		txtTotalStockValue.setText(String.valueOf(totalStockValue));
	
		float totalStockValueAtAddStock=0;
			
		try {		
			String sqlChungKhoan="select * from ChungKhoan";
			preStatement=conn.prepareStatement(sqlChungKhoan);	
			result=preStatement.executeQuery();
			dtmStock.setRowCount(0);
	
			int i=1;
			while(result.next())
			{
				Vector<Object> vecStockTable=new Vector<Object>();
				
				vecStockTable.add(i);
				i++;
				vecStockTable.add(result.getString("Ngay"));
				vecStockTable.add(result.getString("MaVaChiSoCK"));
				vecStockTable.add(result.getFloat("Gia"));
				vecStockTable.add(result.getInt("SoLuong"));
	
				float price=result.getFloat("Gia");//price
				float quantity=result.getInt("SoLuong");//quantity
				float money=price*quantity;//count money
				
				DecimalFormat df = new DecimalFormat("#.00");//format count money
				vecStockTable.add(df.format(money));//count money
				
				totalStockValueAtAddStock+=money;
				
				dtmStock.addRow(vecStockTable);
	
			}
			float soDuTienMat=Float.parseFloat(txtCashBalance.getText());
			float totalStockValueAfterAddStock=totalStockValue+totalStockValueAtAddStock;
			float totalAssestsAfterAddStock=soDuTienMat+totalStockValueAfterAddStock;
			txtTotalStockValue.setText(String.valueOf(totalStockValueAfterAddStock));
			txtTotalAssets.setText(String.valueOf(totalAssestsAfterAddStock));
	
		}catch(NullPointerException e){
			System.out.println(""+e);
		} catch (SQLException e) {
			System.out.println(""+e);
		}catch(Exception e){
			e.printStackTrace();
		}
	}	

	public void displayStockAfterUpdateStock() {
		try {		
			String sql="select * from ChungKhoan where SoTK=? and LoaiCKHayCS=?";
			preStatement=conn.prepareStatement(sql);
			preStatement.setString(1, txtAccountNumber.getText());
			preStatement.setInt(2, 0);
			result=preStatement.executeQuery();
			dtmStock.setRowCount(0);

			DecimalFormat df = new DecimalFormat("#.00");

			float totalStockValue = 0;

			for (int i = 0; i < tblStock.getRowCount(); i++){
				float giaTriCK = Float.parseFloat((String) tblStock.getValueAt(i, 5));
				totalStockValue += giaTriCK;
			}
			txtTotalStockValue.setText(String.valueOf(totalStockValue));

			float totalStockValueAtAddStock=0;

			int id=1;
			while(result.next())
			{
				Vector<Object> vecStockTable=new Vector<Object>();
				vecStockTable.add(id);
				id++;
				vecStockTable.add(result.getString("Ngay"));
				vecStockTable.add(result.getString("MaVaChiSoCK"));
				vecStockTable.add(df.format(result.getFloat("Gia")));
				vecStockTable.add(result.getInt("SoLuong"));

				float price=result.getFloat("Gia");
				float quantity=result.getInt("SoLuong");
				float money=price*quantity;

				totalStockValueAtAddStock+=money;

				vecStockTable.add(df.format(money));
				dtmStock.addRow(vecStockTable);
			}
			float cashBalance=Float.parseFloat(txtCashBalance.getText());
			float totalStockValueAfterAddStock=totalStockValue+totalStockValueAtAddStock;
			float totalAssestsAfterAddStock=cashBalance+totalStockValueAfterAddStock;
			txtTotalStockValue.setText(String.valueOf(totalStockValueAfterAddStock));
			txtTotalAssets.setText(String.valueOf(totalAssestsAfterAddStock));

		}catch(Exception e){
			e.printStackTrace();
		}	
	}	

	public void displayListOfAccountNumber() {		
		closeInputData();//only view
		try {		
			Vector<Account>vecAccount=new Vector<Account>();
			try {
				String sql="select * from TaiKhoan";
				statement = conn.createStatement();
				result = statement.executeQuery(sql);
				while(result.next())
				{
					Account tk=new Account();
					tk.setSoTK(result.getString(1));
					tk.setTenTK(result.getString(2));
					tk.setNamSinh(result.getString(3));
					tk.setSoDT(result.getString(4));
					tk.setSoCMND(result.getString(5));
					tk.setEmail(result.getString(6));
					tk.setDiaChi(result.getString(7));					
					tk.setSoDuTienMatBD(result.getInt(8));
					tk.setTongTaiSanBD(result.getFloat(9));
					vecAccount.add(tk);
					listAccount.setListData(vecAccount);		
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connectDatabase(){
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl="jdbc:sqlserver://"+strServer+":1433;databaseName="+strDatabase+";integratedSecurity=true;";
			conn=DriverManager.getConnection(connectionUrl);
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}

	
	private void addControls() {
		Container con=getContentPane();
		con.setLayout(new BorderLayout());

		JPanel pnLeft=new JPanel();
		pnLeft.setPreferredSize(new Dimension(250, 0));
		JPanel pnRight=new JPanel();

		JSplitPane sp=new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,pnLeft,pnRight);
		sp.setOneTouchExpandable(true);
		con.add(sp,BorderLayout.CENTER);

		pnLeft.setLayout(new BorderLayout());
		listAccount=new JList<Account>();
		JScrollPane sclistAccount=new JScrollPane(
				listAccount,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnLeft.add(sclistAccount,BorderLayout.CENTER);

		TitledBorder borderTitle=new TitledBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				"Danh sách tài khoản");
		borderTitle.setTitleColor(Color.BLUE);
		borderTitle.setTitleJustification(TitledBorder.CENTER);
		listAccount.setBorder(borderTitle);

		JPanel pnBtnAccount=new JPanel();
		btnAddAccount=new JButton("Thêm TK");
		btnDeleteAccount=new JButton("Xóa TK");
		btnEditAccount=new JButton("Sửa TK");
		btnUpdateAccount=new JButton("Cập nhật TK");
		pnBtnAccount.add(btnAddAccount);
		pnBtnAccount.add(btnDeleteAccount);
		pnBtnAccount.add(btnEditAccount);
		pnBtnAccount.add(btnUpdateAccount);
		pnLeft.add(pnBtnAccount,BorderLayout.SOUTH);

		pnRight.setLayout(new BorderLayout());
		JPanel pnTopOfRight=new JPanel();
		pnTopOfRight.setLayout(new BoxLayout(
				pnTopOfRight,BoxLayout.Y_AXIS));	
		pnRight.add(pnTopOfRight,BorderLayout.NORTH);	

		JPanel pnTitle=new JPanel();
		pnTitle.setLayout(new FlowLayout());
		JLabel lblTitle=new JLabel("Thông tin chi tiết tài khoản");
		lblTitle.setForeground(Color.BLUE);
		pnTitle.add(lblTitle);
		pnTopOfRight.add(pnTitle);

		JPanel pnTopOfRightLine1=new JPanel();
		pnTopOfRightLine1.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblAccountName=new JLabel("1. Tên TK:");
		txtAccountName=new JTextField(12);
		JLabel lblYOB=new JLabel("2. Năm sinh:");
		txtYOB=new JTextField(12);
		JLabel lblMobile=new JLabel("3. Số ĐT:");
		txtMobile=new JTextField(12);
		pnTopOfRightLine1.add(lblAccountName);
		pnTopOfRightLine1.add(txtAccountName);
		pnTopOfRightLine1.add(lblYOB);
		pnTopOfRightLine1.add(txtYOB);	
		pnTopOfRightLine1.add(lblMobile);
		pnTopOfRightLine1.add(txtMobile);
		pnTopOfRight.add(pnTopOfRightLine1);

		JPanel pnTopOfRightLine2=new JPanel();
		pnTopOfRightLine2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblIdentityCardNumber=new JLabel("4. Số CMND:");
		txtIdentityCardNumber=new JTextField(12);
		JLabel lblEmail=new JLabel("5. Email:");
		txtEmail=new JTextField(12);
		JLabel lblStartDay=new JLabel("6. Ngày bắt đầu:");
		txtStartDay=new JTextField(12);
		pnTopOfRightLine2.add(lblIdentityCardNumber);
		pnTopOfRightLine2.add(txtIdentityCardNumber);
		pnTopOfRightLine2.add(lblEmail);
		pnTopOfRightLine2.add(txtEmail);
		pnTopOfRightLine2.add(lblStartDay);
		pnTopOfRightLine2.add(txtStartDay);
		pnTopOfRight.add(pnTopOfRightLine2);
		
		JPanel pnTopOfRightLine3=new JPanel();
		pnTopOfRightLine3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblAddress=new JLabel("6. Địa chỉ:");
		txtAddress=new JTextField(56);
		pnTopOfRightLine3.add(lblAddress);
		pnTopOfRightLine3.add(txtAddress);
		pnTopOfRight.add(pnTopOfRightLine3);
			
		JPanel pnTopOfRightLine4=new JPanel();
		pnTopOfRightLine4.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblAccountNumber=new JLabel("7. Số TK:");
		txtAccountNumber=new JTextField(12);
		JLabel lblCashBalance=new JLabel("8. Số dư tiền mặt:");
		txtCashBalance=new JTextField(12);
		JLabel lblTotalAssets=new JLabel("9. Tổng tài sản:");
		txtTotalAssets=new JTextField(12);
		pnTopOfRightLine4.add(lblAccountNumber);
		pnTopOfRightLine4.add(txtAccountNumber);
		pnTopOfRightLine4.add(lblCashBalance);
		pnTopOfRightLine4.add(txtCashBalance);
		pnTopOfRightLine4.add(lblTotalAssets);
		pnTopOfRightLine4.add(txtTotalAssets);
		pnTopOfRight.add(pnTopOfRightLine4);		

		JPanel pnTopOfRightLine5=new JPanel();
		pnTopOfRightLine5.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblStockCode=new JLabel("11. Mã CK:");
		txtStockCode=new JTextField(12);
		JLabel lblPrice=new JLabel("12. Giá:");
		txtPrice=new JTextField(12);
		JLabel lblQuantity=new JLabel("13. Số lượng:");
		txtQuantity=new JTextField(12);
		pnTopOfRightLine5.add(lblStockCode);
		pnTopOfRightLine5.add(txtStockCode);
		pnTopOfRightLine5.add(lblPrice);
		pnTopOfRightLine5.add(txtPrice);
		pnTopOfRightLine5.add(lblQuantity);
		pnTopOfRightLine5.add(txtQuantity);	
		pnTopOfRight.add(pnTopOfRightLine5);
		
		lblAccountName.setPreferredSize(lblIdentityCardNumber.getPreferredSize());
		lblAddress.setPreferredSize(lblIdentityCardNumber.getPreferredSize());
		lblAccountNumber.setPreferredSize(lblIdentityCardNumber.getPreferredSize());
		lblStockCode.setPreferredSize(lblIdentityCardNumber.getPreferredSize());
		lblYOB.setPreferredSize(lblCashBalance.getPreferredSize());		
		lblEmail.setPreferredSize(lblCashBalance.getPreferredSize());
		lblPrice.setPreferredSize(lblCashBalance.getPreferredSize());
		lblMobile.setPreferredSize(lblStartDay.getPreferredSize());		
		lblTotalAssets.setPreferredSize(lblStartDay.getPreferredSize());
		lblQuantity.setPreferredSize(lblStartDay.getPreferredSize());

		JPanel pnBtnStock=new JPanel();
		pnBtnStock.setLayout(new FlowLayout(FlowLayout.LEFT));
		btnAddStock=new JButton("Thêm CK");
		btnDeleteStock=new JButton("Xóa CK");
		btnEditStock=new JButton("Sửa CK");
		btnUpdateStock=new JButton("Cập nhật CK");
		pnBtnStock.add(btnAddStock);
		pnBtnStock.add(btnDeleteStock);
		pnBtnStock.add(btnEditStock);
		pnBtnStock.add(btnUpdateStock);	
		pnTopOfRight.add(pnBtnStock);	

		JPanel pnBottomOfRight=new JPanel();
		pnBottomOfRight.setLayout(new BorderLayout());
		pnRight.add(pnBottomOfRight,BorderLayout.SOUTH);
		pnBottomOfRight.setPreferredSize(new Dimension(0, 400));

		dtmStock=new DefaultTableModel();

		dtmStock.addColumn("Stt");//stt is index and auto increment, not already exists in database
		dtmStock.addColumn("Ngày cập nhật CK");
		dtmStock.addColumn("Mã CK");
		dtmStock.addColumn("Giá");
		dtmStock.addColumn("Số lượng");
		dtmStock.addColumn("Thành tiền");
		tblStock=new JTable(dtmStock);
		tblStock.setPreferredSize(new Dimension(0, 350));
		JScrollPane scTableStock=new JScrollPane(
				tblStock,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnBottomOfRight.add(scTableStock,BorderLayout.CENTER);		

		JPanel pnButtonCK=new JPanel();
		pnButtonCK.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JLabel lblTotalStockValue=new JLabel("Tổng giá trị chứng khoán:");
		txtTotalStockValue=new JTextField(12);
		btnExit=new JButton("Thoát");	
		pnButtonCK.add(lblTotalStockValue);
		pnButtonCK.add(txtTotalStockValue);
		pnButtonCK.add(btnExit);
		pnBottomOfRight.add(pnButtonCK,BorderLayout.SOUTH);

	}

	public void showWindow()
	{
		this.setSize(1090,650);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}

/*------------------- Finish the way the interface is handled -------------------*/
	
}
