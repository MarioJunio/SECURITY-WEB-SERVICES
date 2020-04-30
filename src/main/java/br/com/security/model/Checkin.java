package br.com.security.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.security.rest.stub.SyncCheckin;

@Entity
@Table(name = "checkin")
public class Checkin implements Serializable {

	private Long id;
	private Empregado empregado;
	private Cliente cliente;
	private Date data;
	private double latitude;
	private double longitude;
	private CheckinStatus status;
	private String descricao;
	private Long timeSync;

	public Checkin() {
	}

	public Checkin(SyncCheckin sc, Long timeSync) {
		setEmpregado(new Empregado(sc.getFuncionarioId()));
		setCliente(new Cliente(sc.getCliente()));
		setData(sc.getData());
		setLatitude(sc.getLatitude());
		setLongitude(sc.getLongitude());
		setStatus(sc.getStatus());
		setDescricao(sc.getDescricao());
		setTimeSync(timeSync);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "empregado_id")
	public Empregado getEmpregado() {
		return empregado;
	}

	public void setEmpregado(Empregado empregado) {
		this.empregado = empregado;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id")
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Column
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Column
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Column
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Enumerated(EnumType.STRING)
	@Column
	public CheckinStatus getStatus() {
		return status;
	}

	public void setStatus(CheckinStatus status) {
		this.status = status;
	}

	@Lob
	@Column
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "time_sync")
	public Long getTimeSync() {
		return timeSync;
	}

	public void setTimeSync(Long timeSync) {
		this.timeSync = timeSync;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Checkin other = (Checkin) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Checkin [id=" + id + ", empregado=" + empregado + ", cliente=" + cliente + ", data=" + data + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", status=" + status + ", descricao=" + descricao + ", timeSync=" + timeSync + "]";
	}

}
