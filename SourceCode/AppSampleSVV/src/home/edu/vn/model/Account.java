package home.edu.vn.model;

public class Account {
	private String soTK;
	private String tenTK;
	private String namSinh;
	private String soCMND;
	private String diaChi;
	private String soDT;
	private String email;
	private int soDuTienMatBD;
	private float tongTaiSanBD;

	public String getSoTK() {return soTK;}
	public void setSoTK(String soTK) {this.soTK = soTK;}
	
	public String getTenTK() {return tenTK;}
	public void setTenTK(String tenTK) {this.tenTK = tenTK;}
	
	public String getNamSinh() {return namSinh;}
	public void setNamSinh(String namSinh) {this.namSinh = namSinh;}
	
	public String getSoCMND() {return soCMND;}
	public void setSoCMND(String soCMND) {this.soCMND = soCMND;}
	
	public String getDiaChi() {return diaChi;}
	public void setDiaChi(String diaChi) {this.diaChi = diaChi;}
	
	public String getSoDT() {return soDT;}
	public void setSoDT(String soDT) {this.soDT = soDT;}
	
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	
	public int getSoDuTienMatBD() {return soDuTienMatBD;}
	public void setSoDuTienMatBD(int soDuTienMatBD) {this.soDuTienMatBD = soDuTienMatBD;}
	
	public float getTongTaiSanBD() {return tongTaiSanBD;}
	public void setTongTaiSanBD(float tongTaiSanBD) {this.tongTaiSanBD = tongTaiSanBD;}
	
	@Override
	public String toString() {return this.soTK;}
}
