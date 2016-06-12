package com.rc2s.pluginname.common.vo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Bot implements Serializable
{
	@Id
	private Integer id;
}
