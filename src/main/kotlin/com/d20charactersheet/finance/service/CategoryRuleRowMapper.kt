package com.d20charactersheet.finance.service

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class CategoryRuleRowMapper : RowMapper<CategoryRule> {

    override fun mapRow(rs: ResultSet, rowNum: Int): CategoryRule = CategoryRule(
        rs.getString("CategoryText"),
        rs.getInt("CategoryId")
    )

}
