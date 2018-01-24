package com.hafu.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 体检信息--心电数据
 * @author Darker
 *
 */
public class CheckDataWaveFormDto  implements Serializable{

	private static final long serialVersionUID = 5393963643852463058L;
	@JsonProperty(value = "Sample")  
	private String Sample;				//心电波形采样率
	@JsonProperty(value = "P05")  
	private String P05;					//+0.5mv对应的数值
	@JsonProperty(value = "N05")  
	private String N05;					//-0.5mv对应的数值
	@JsonProperty(value = "Duration")  
	private String Duration;			//波形持续时间
	@JsonProperty(value = "ECG__I") 
	private String ECG_I;				//心电I波形
	@JsonProperty(value = "ECG__II") 
	private String ECG_II;				//心电II波形
	@JsonProperty(value = "ECG__III") 
	private String ECG_III;				//心电III波形
	@JsonProperty(value = "ECG__aVR") 
	private String ECG_aVR;				//心电aVR波形
	@JsonProperty(value = "ECG__aVF") 
	private String ECG_aVF;				//心电aVF波形
	@JsonProperty(value = "ECG__aVL") 
	private String ECG_aVL;				//心电aVL波形
	@JsonProperty(value = "ECG__V1") 
	private String ECG_V1;				//心电V1波形
	@JsonProperty(value = "ECG__V2") 
	private String ECG_V2;				//心电V2波形
	@JsonProperty(value = "ECG__V3") 
	private String ECG_V3;				//心电V3波形
	@JsonProperty(value = "ECG__V4") 
	private String ECG_V4;				//心电V4波形
	@JsonProperty(value = "ECG__V5") 
	private String ECG_V5;				//心电V5波形
	@JsonProperty(value = "ECG__V6") 
	private String ECG_V6;				//心电V6波形
	
	public String getSample() {
		return Sample;
	}
	public void setSample(String sample) {
		Sample = sample;
	}
	public String getP05() {
		return P05;
	}
	public void setP05(String p05) {
		P05 = p05;
	}
	public String getN05() {
		return N05;
	}
	public void setN05(String n05) {
		N05 = n05;
	}
	public String getDuration() {
		return Duration;
	}
	public void setDuration(String duration) {
		Duration = duration;
	}
	public String getECG_I() {
		return ECG_I;
	}
	public void setECG_I(String eCG_I) {
		ECG_I = eCG_I;
	}
	public String getECG_II() {
		return ECG_II;
	}
	public void setECG_II(String eCG_II) {
		ECG_II = eCG_II;
	}
	public String getECG_III() {
		return ECG_III;
	}
	public void setECG_III(String eCG_III) {
		ECG_III = eCG_III;
	}
	public String getECG_aVR() {
		return ECG_aVR;
	}
	public void setECG_aVR(String eCG_aVR) {
		ECG_aVR = eCG_aVR;
	}
	public String getECG_aVF() {
		return ECG_aVF;
	}
	public void setECG_aVF(String eCG_aVF) {
		ECG_aVF = eCG_aVF;
	}
	public String getECG_aVL() {
		return ECG_aVL;
	}
	public void setECG_aVL(String eCG_aVL) {
		ECG_aVL = eCG_aVL;
	}
	public String getECG_V1() {
		return ECG_V1;
	}
	public void setECG_V1(String eCG_V1) {
		ECG_V1 = eCG_V1;
	}
	public String getECG_V2() {
		return ECG_V2;
	}
	public void setECG_V2(String eCG_V2) {
		ECG_V2 = eCG_V2;
	}
	public String getECG_V3() {
		return ECG_V3;
	}
	public void setECG_V3(String eCG_V3) {
		ECG_V3 = eCG_V3;
	}
	public String getECG_V4() {
		return ECG_V4;
	}
	public void setECG_V4(String eCG_V4) {
		ECG_V4 = eCG_V4;
	}
	public String getECG_V5() {
		return ECG_V5;
	}
	public void setECG_V5(String eCG_V5) {
		ECG_V5 = eCG_V5;
	}
	public String getECG_V6() {
		return ECG_V6;
	}
	public void setECG_V6(String eCG_V6) {
		ECG_V6 = eCG_V6;
	}
	@Override
	public String toString() {
		return "ExaminationWaveForm [Sample=" + Sample + ", P05=" + P05
				+ ", N05=" + N05 + ", Duration=" + Duration + ", ECG_I="
				+ ECG_I + ", ECG_II=" + ECG_II + ", ECG_III=" + ECG_III
				+ ", ECG_aVR=" + ECG_aVR + ", ECG_aVF=" + ECG_aVF
				+ ", ECG_aVL=" + ECG_aVL + ", ECG_V1=" + ECG_V1 + ", ECG_V2="
				+ ECG_V2 + ", ECG_V3=" + ECG_V3 + ", ECG_V4=" + ECG_V4
				+ ", ECG_V5=" + ECG_V5 + ", ECG_V6=" + ECG_V6 + "]";
	}
	
}
