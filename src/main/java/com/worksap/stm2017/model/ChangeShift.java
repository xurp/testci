package com.worksap.stm2017.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

@Entity 
public class ChangeShift {
	//this class "may should" be many to one to user. but here, userid and changeid can be both associate to user
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id; 
	
	@Column(nullable = false, length = 50) 
	private Long userid;
	
	@Column(nullable = false, length = 50) 
	private Long changeid;
	
	@Column(nullable = false, length = 50) 
	private String username;
	
	@Column(nullable = false, length = 50) 
	private String changename;
	
	@Column(nullable = false, length = 50) 
	private String isAgreed;
	
	@Column(nullable = false, length = 50) 
	private String isChanged;
	
	@Column(nullable = true, length = 1000) 
	private String reason;
	
	@NotEmpty(message = "file name can not be empty")
	@Column(nullable = false, length = 50) 
	private String filename;
	@NotEmpty(message = "date can not be empty")
	@Column(nullable = false, length = 50) 
	private String date;
	@Column(nullable = false, length = 50) 
	private String shiftname;
	@NotEmpty(message = "his/her date can not be empty")
	@Column(nullable = false, length = 50) 
	private String todate;
	@Column(nullable = false, length = 50) 
	private String toshiftname;
	
	
	
	@Column(nullable=false)
	@CreationTimestamp
	private Timestamp createTime;
	
	@Column(nullable=true)
	private Timestamp operTime;
	
    protected ChangeShift(){}

	public ChangeShift(Long userid, Long changeid, String username, String changename,String isAgreed, String isChanged,String reason,
			String filename,String date,String shiftname, String todate,String toshiftname) {
		super();
		this.userid = userid;
		this.changeid = changeid;
		this.isAgreed = isAgreed;
		this.isChanged = isChanged;
		this.username=username;
		this.changename=changename;
		this.reason=reason;
		this.filename=filename;
		this.date=date;
		this.shiftname=shiftname;
		this.todate=todate;
		this.toshiftname=toshiftname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getChangeid() {
		return changeid;
	}

	public void setChangeid(Long changeid) {
		this.changeid = changeid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChangename() {
		return changename;
	}

	public void setChangename(String changename) {
		this.changename = changename;
	}

	public String getIsAgreed() {
		return isAgreed;
	}

	public void setIsAgreed(String isAgreed) {
		this.isAgreed = isAgreed;
	}

	public String getIsChanged() {
		return isChanged;
	}

	public void setIsChanged(String isChanged) {
		this.isChanged = isChanged;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getOperTime() {
		return operTime;
	}

	public void setOperTime(Timestamp operTime) {
		this.operTime = operTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getShiftname() {
		return shiftname;
	}

	public void setShiftname(String shiftname) {
		this.shiftname = shiftname;
	}

	public String getTodate() {
		return todate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public String getToshiftname() {
		return toshiftname;
	}

	public void setToshiftname(String toshiftname) {
		this.toshiftname = toshiftname;
	}
	
	
    
}
