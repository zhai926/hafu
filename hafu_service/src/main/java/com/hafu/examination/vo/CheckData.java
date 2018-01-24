package com.hafu.examination.vo;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CheckData {

	private String id;				//编号
	private String uploadDate;		//上传时间yyyy-MM-dd hh:mm:ss
	@JsonProperty(value = "IcpName")  
	private String IcpName;			//检查机构名称
	@JsonProperty(value = "DeviceID")  
	private String DeviceID;		//一体机编号
	@JsonProperty(value = "CardNo")
	private String CardNo;			//身份证号
	@JsonProperty(value = "Name")
	private String Name;			//姓名
	@JsonProperty(value = "Sex")
	private String Sex;				//性别 0未知 1男 2女
	@JsonProperty(value = "Birthday")
	private String Birthday;		//生日 yyyy-MM-dd
	@JsonProperty(value = "Doctor")
	private String Doctor;			//责任医生
	@JsonProperty(value = "CheckDate")
	private String CheckDate;		//检查时间yyyy-MM-dd hh:mm:ss
	@JsonProperty(value = "HR")
	private String HR;				//心率 分钟/次
	@JsonProperty(value = "SBP")
	private String SBP;				//收缩压 mmHg
	@JsonProperty(value = "DBP")
	private String DBP;				//舒张压 mmHg
	@JsonProperty(value = "MBP")
	private String MBP;				//平均压 mmHg
	@JsonProperty(value = "SPO2")
	private String SPO2;			//血氧饱和度 %
	@JsonProperty(value = "PR")
	private String PR;				//脉率 分钟/次
	@JsonProperty(value = "Glu")
	private String Glu;				//血糖 mmol/L
	@JsonProperty(value = "Height")
	private String Height;			//身高 cm
	@JsonProperty(value = "Weight")
	private String Weight;			//体重 kg
	@JsonProperty(value = "Temp")
	private String Temp;			//体温 摄氏度
	@JsonProperty(value = "Urine__ph")
	private String Urine__ph;		//尿常规酸碱度
	@JsonProperty(value = "Urine__ubg")
	private String Urine__ubg;		//尿常规尿胆原
	@JsonProperty(value = "Urine__bld")
	private String Urine__bld;		//尿常规隐血
	@JsonProperty(value = "Urine__pro")
	private String Urine__pro;		//尿常规尿蛋白
	@JsonProperty(value = "Urine__ket")
	private String Urine__ket;		//尿常规酮体
	@JsonProperty(value = "Urine__nit")
	private String Urine__nit;		//尿常规亚硝酸盐
	@JsonProperty(value = "Urine__glu")
	private String Urine__glu;		//尿常规尿糖
	@JsonProperty(value = "Urine__bil")
	private String Urine__bil;		//尿常规胆红素
	@JsonProperty(value = "Urine__leu")
	private String Urine__leu;		//尿常规白细胞
	@JsonProperty(value = "Urine__sg")
	private String Urine__sg;		//尿常规尿比密
	@JsonProperty(value = "Urine__vc")
	private String Urine__vc;		//尿常规维生素c
	@JsonProperty(value = "Urine__cre")
	private String Urine__cre;		
	@JsonProperty(value = "Urine__ca")
	private String Urine__ca;
	@JsonProperty(value = "Anal")
	private String Anal;			//心电图自动诊断结果
	@JsonProperty(value = "BMI")
	private String BMI;				//体质指数
	@JsonProperty(value = "WAIST")
	private String WAIST;			//腰围 (cm)
	@JsonProperty(value = "ASSKFXTL")
	private String ASSKFXTL;		//空腹血糖 (mmol/L)
	@JsonProperty(value = "ASSXHDB")
	private String ASSXHDB;			//血常规--血红蛋白 (g/L)
	@JsonProperty(value = "ASSBXB")
	private String ASSBXB;			//血常规--白细胞
	@JsonProperty(value = "ASSXXB")
	private String ASSXXB;			//血常规--血小板
	@JsonProperty(value = "ASSKFXTDL")
	private String ASSKFXTDL;		//空腹血糖 (mg/dL)
	@JsonProperty(value = "ASSHXBYJZ")
	private String ASSHXBYJZ;
	@JsonProperty(value = "URICACID")
	private String URICACID;
	@JsonProperty(value = "ASSWLDB")
	private String ASSWLDB;			//尿微量白蛋白 （mg/dL）
	@JsonProperty(value = "ASSTHXHDB")
	private String ASSTHXHDB;		//辅助检查--糖化血红蛋白 （%）
	@JsonProperty(value = "GGNXQGB")
	private String GGNXQGB;			//肝功能--血清谷丙转氨酶 (U/L)
	@JsonProperty(value = "GGNXQGC")
	private String GGNXQGC;			//肝功能--血清谷草转氨酶 (U/L)
	@JsonProperty(value = "GGNBDB")
	private String GGNBDB;			//肝功能--白蛋白 (g/L )
	@JsonProperty(value = "GGNZDHS")
	private String GGNZDHS;			//肝功能--总胆红素 (μmol/L)
	@JsonProperty(value = "SGNXQJ")
	private String SGNXQJ;			//肾功能--血清肌酐 (μmol/L)
	@JsonProperty(value = "SGNXNSD")
	private String SGNXNSD;			//肾功能--血尿素氮 (mmol/L)
	@JsonProperty(value = "SGNXJND")
	private String SGNXJND;			//肾功能--血钾浓度 (mmol/L)
	@JsonProperty(value = "SGNXNND")
	private String SGNXNND;			//肾功能--血钠浓度 (mmol/L)
	@JsonProperty(value = "XZZDGC")
	private String XZZDGC;			//血脂--总胆固醇 (mmol/L)
	@JsonProperty(value = "XZGYSZ")
	private String XZGYSZ;			//血脂--甘油三酯 (mmol/L)
	@JsonProperty(value = "XZGQD")
	private String XZGQD;			//血脂--血清低密度脂蛋白胆固醇 (mmol/L)
	@JsonProperty(value = "RESP__RR")
	private String RESP__RR;
	@JsonProperty(value = "WaveForm")
	private CheckDataWaveForm WaveForm;	//心电数据
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUploadDate() {
		return uploadDate;
	}


	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getIcpName() {
		return IcpName;
	}

	public void setIcpName(String icpName) {
		IcpName = icpName;
	}


	public String getDeviceID() {
		return DeviceID;
	}


	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}


	public String getCardNo() {
		return CardNo;
	}


	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}


	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	public String getSex() {
		return Sex;
	}


	public void setSex(String sex) {
		Sex = sex;
	}


	public String getBirthday() {
		return Birthday;
	}


	public void setBirthday(String birthday) {
		Birthday = birthday;
	}


	public String getDoctor() {
		return Doctor;
	}


	public void setDoctor(String doctor) {
		Doctor = doctor;
	}


	public String getCheckDate() {
		return CheckDate;
	}


	public void setCheckDate(String checkDate) {
		CheckDate = checkDate;
	}


	public String getHR() {
		return HR;
	}


	public void setHR(String hR) {
		HR = hR;
	}


	public String getSBP() {
		return SBP;
	}


	public void setSBP(String sBP) {
		SBP = sBP;
	}


	public String getDBP() {
		return DBP;
	}


	public void setDBP(String dBP) {
		DBP = dBP;
	}


	public String getMBP() {
		return MBP;
	}


	public void setMBP(String mBP) {
		MBP = mBP;
	}


	public String getSPO2() {
		return SPO2;
	}


	public void setSPO2(String sPO2) {
		SPO2 = sPO2;
	}


	public String getPR() {
		return PR;
	}


	public void setPR(String pR) {
		PR = pR;
	}


	public String getGlu() {
		return Glu;
	}


	public void setGlu(String glu) {
		Glu = glu;
	}


	public String getHeight() {
		return Height;
	}


	public void setHeight(String height) {
		Height = height;
	}


	public String getWeight() {
		return Weight;
	}


	public void setWeight(String weight) {
		Weight = weight;
	}


	public String getTemp() {
		return Temp;
	}


	public void setTemp(String temp) {
		Temp = temp;
	}


	public String getUrine__ph() {
		return Urine__ph;
	}


	public void setUrine__ph(String urine__ph) {
		Urine__ph = urine__ph;
	}


	public String getUrine__ubg() {
		return Urine__ubg;
	}


	public void setUrine__ubg(String urine__ubg) {
		Urine__ubg = urine__ubg;
	}


	public String getUrine__bld() {
		return Urine__bld;
	}


	public void setUrine__bld(String urine__bld) {
		Urine__bld = urine__bld;
	}


	public String getUrine__pro() {
		return Urine__pro;
	}


	public void setUrine__pro(String urine__pro) {
		Urine__pro = urine__pro;
	}


	public String getUrine__ket() {
		return Urine__ket;
	}


	public void setUrine__ket(String urine__ket) {
		Urine__ket = urine__ket;
	}


	public String getUrine__nit() {
		return Urine__nit;
	}


	public void setUrine__nit(String urine__nit) {
		Urine__nit = urine__nit;
	}


	public String getUrine__glu() {
		return Urine__glu;
	}


	public void setUrine__glu(String urine__glu) {
		Urine__glu = urine__glu;
	}


	public String getUrine__bil() {
		return Urine__bil;
	}


	public void setUrine__bil(String urine__bil) {
		Urine__bil = urine__bil;
	}


	public String getUrine__leu() {
		return Urine__leu;
	}


	public void setUrine__leu(String urine__leu) {
		Urine__leu = urine__leu;
	}


	public String getUrine__sg() {
		return Urine__sg;
	}


	public void setUrine__sg(String urine__sg) {
		Urine__sg = urine__sg;
	}


	public String getUrine__vc() {
		return Urine__vc;
	}


	public void setUrine__vc(String urine__vc) {
		Urine__vc = urine__vc;
	}


	public String getUrine__cre() {
		return Urine__cre;
	}


	public void setUrine__cre(String urine__cre) {
		Urine__cre = urine__cre;
	}


	public String getUrine__ca() {
		return Urine__ca;
	}


	public void setUrine__ca(String urine__ca) {
		Urine__ca = urine__ca;
	}


	public String getAnal() {
		return Anal;
	}


	public void setAnal(String anal) {
		Anal = anal;
	}


	public String getBMI() {
		return BMI;
	}


	public void setBMI(String bMI) {
		BMI = bMI;
	}


	public String getWAIST() {
		return WAIST;
	}


	public void setWAIST(String wAIST) {
		WAIST = wAIST;
	}


	public String getASSKFXTL() {
		return ASSKFXTL;
	}


	public void setASSKFXTL(String aSSKFXTL) {
		ASSKFXTL = aSSKFXTL;
	}


	public String getASSXHDB() {
		return ASSXHDB;
	}


	public void setASSXHDB(String aSSXHDB) {
		ASSXHDB = aSSXHDB;
	}


	public String getASSBXB() {
		return ASSBXB;
	}


	public void setASSBXB(String aSSBXB) {
		ASSBXB = aSSBXB;
	}


	public String getASSXXB() {
		return ASSXXB;
	}


	public void setASSXXB(String aSSXXB) {
		ASSXXB = aSSXXB;
	}


	public String getASSKFXTDL() {
		return ASSKFXTDL;
	}


	public void setASSKFXTDL(String aSSKFXTDL) {
		ASSKFXTDL = aSSKFXTDL;
	}


	public String getASSHXBYJZ() {
		return ASSHXBYJZ;
	}


	public void setASSHXBYJZ(String aSSHXBYJZ) {
		ASSHXBYJZ = aSSHXBYJZ;
	}


	public String getURICACID() {
		return URICACID;
	}


	public void setURICACID(String uRICACID) {
		URICACID = uRICACID;
	}


	public String getASSWLDB() {
		return ASSWLDB;
	}


	public void setASSWLDB(String aSSWLDB) {
		ASSWLDB = aSSWLDB;
	}


	public String getASSTHXHDB() {
		return ASSTHXHDB;
	}


	public void setASSTHXHDB(String aSSTHXHDB) {
		ASSTHXHDB = aSSTHXHDB;
	}


	public String getGGNXQGB() {
		return GGNXQGB;
	}


	public void setGGNXQGB(String gGNXQGB) {
		GGNXQGB = gGNXQGB;
	}


	public String getGGNXQGC() {
		return GGNXQGC;
	}


	public void setGGNXQGC(String gGNXQGC) {
		GGNXQGC = gGNXQGC;
	}


	public String getGGNBDB() {
		return GGNBDB;
	}


	public void setGGNBDB(String gGNBDB) {
		GGNBDB = gGNBDB;
	}


	public String getGGNZDHS() {
		return GGNZDHS;
	}


	public void setGGNZDHS(String gGNZDHS) {
		GGNZDHS = gGNZDHS;
	}


	public String getSGNXQJ() {
		return SGNXQJ;
	}


	public void setSGNXQJ(String sGNXQJ) {
		SGNXQJ = sGNXQJ;
	}


	public String getSGNXNSD() {
		return SGNXNSD;
	}


	public void setSGNXNSD(String sGNXNSD) {
		SGNXNSD = sGNXNSD;
	}


	public String getSGNXJND() {
		return SGNXJND;
	}


	public void setSGNXJND(String sGNXJND) {
		SGNXJND = sGNXJND;
	}


	public String getSGNXNND() {
		return SGNXNND;
	}


	public void setSGNXNND(String sGNXNND) {
		SGNXNND = sGNXNND;
	}


	public String getXZZDGC() {
		return XZZDGC;
	}


	public void setXZZDGC(String xZZDGC) {
		XZZDGC = xZZDGC;
	}


	public String getXZGYSZ() {
		return XZGYSZ;
	}


	public void setXZGYSZ(String xZGYSZ) {
		XZGYSZ = xZGYSZ;
	}


	public String getXZGQD() {
		return XZGQD;
	}


	public void setXZGQD(String xZGQD) {
		XZGQD = xZGQD;
	}


	public String getRESP__RR() {
		return RESP__RR;
	}


	public void setRESP__RR(String rESP__RR) {
		RESP__RR = rESP__RR;
	}


	public CheckDataWaveForm getWaveForm() {
		return WaveForm;
	}


	public void setWaveForm(CheckDataWaveForm waveForm) {
		WaveForm = waveForm;
	}

}
