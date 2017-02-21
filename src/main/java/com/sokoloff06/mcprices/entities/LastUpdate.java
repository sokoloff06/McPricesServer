package com.sokoloff06.mcprices.entities;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Vitaly Sokolov on 14.02.2017.
 */
@Entity
public class LastUpdate {
	@Id
	private int id;
	private Timestamp timestamp;

	public LastUpdate(int id, Timestamp timestamp) {
		this.id = id;
		this.timestamp = timestamp;
	}

	public LastUpdate() {
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LastUpdate that = (LastUpdate) o;

		if (id != that.id) return false;
		return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;
	}

	@Override
	public int hashCode() {
		int result = timestamp != null ? timestamp.hashCode() : 0;
		result = 31 * result + id;
		return result;
	}
}
