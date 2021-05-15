package com.d20charactersheet.finance.service

import com.d20charactersheet.finance.domain.MoneyTransfer
import com.d20charactersheet.finance.domain.RawMoneyTransfer
import org.springframework.stereotype.Service

@Service
class MoneyTransferService {

    fun filterNewMoneyTransfers(rawMoneyTransfers : List<RawMoneyTransfer>): List<MoneyTransfer> =
        rawMoneyTransfers.filter { it.hashTag.hashTag == "" }.map { it.toMoneyTransfer() }


}
