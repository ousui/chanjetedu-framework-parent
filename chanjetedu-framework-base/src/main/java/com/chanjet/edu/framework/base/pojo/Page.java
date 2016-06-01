package com.chanjet.edu.framework.base.pojo;

import com.chanjet.edu.framework.base.utils.StringUtils;
import com.google.common.collect.Maps;
import com.google.inject.internal.ToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by shuai.w on 2016/5/26.
 */
public class Page<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private int page = 1;
	@Getter
	@Setter
	private int size = 10;

	private int start = 0;

	@Getter
	private int total = 0;
	@Getter
	private int pages;

	// 无需特意执行这个 setter，已经有拦截器来自动配置。
	@Getter
	@Setter
	private List<T> datas;

	// 排序，规则为 key(field)=value(dir), value 可以忽略，默认为 ASC
	private Map<String, String> orders;

	public Page() {
	}

	public Page(int page, int size) {
		this.page = page;
		this.size = size;
		this.start = page > 0 ? (page - 1) * size : 0;
	}

	public int getStart() {
		return this.page > 0 ? (this.page * this.size - this.size) : 0;
	}

	public void setTotal(int total) {
		this.total = total;
		this.pages = (total / this.size + ((total % this.size == 0) ? 0 : 1));
	}

	public void setOrders(String orders) {
		if (this.orders == null) {
			this.orders = Maps.newLinkedHashMap();
		}
		if (StringUtils.hasText(orders)) {
			String os[] = orders.split(",");
			String kvs[];
			for (String kv : os) {
				Assert.isTrue(kv.matches("[A-z]?\\.?[A-z0-9\\.]+@?([Aa][Ss][Cc]|[Dd][Ee][Ss][Cc])?"), "order param is illegal.");

				String type = "ASC";
				kvs = kv.split("@");
				String field = kvs[0];
				if (kvs.length > 1) {
					type = kvs[1].trim().toUpperCase();
				}

				Assert.isTrue(("ASC".equals(type) || "DESC".equals(type)), "order param is illegal.");

				if (field.contains(".")) {
					field = field.replace(".", ".`");
				} else {
					field = "`" + field;
				}
				this.orders.put(field + "`", type);
			}
		}
	}

	public String getOrders() {
		if (this.orders == null || this.orders.isEmpty()) {
			return "";
		}
		StringBuilder sql = new StringBuilder();
		sql.append(" ORDER BY ");
		for (Map.Entry<String, String> kv : this.orders.entrySet()) {
			sql.append(StringUtils.camel2underline(kv.getKey())).append(" ").append(kv.getValue()).append(", ");
		}
		return sql.delete(sql.lastIndexOf(", "), sql.length()).toString();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this.getClass()).add("page", page).add("size", size).add("start", start).add("total", total)
				.add("pages", pages).add("datas", datas).add("orders", orders).toString();
	}

}
