package io.horizontalsystems.bankwallet.modules.balance.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.horizontalsystems.bankwallet.BuildConfig
import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.App
import io.horizontalsystems.bankwallet.core.navigateWithTermsAccepted
import io.horizontalsystems.bankwallet.core.slideFromRight
import io.horizontalsystems.bankwallet.entities.AccountType
import io.horizontalsystems.bankwallet.modules.enablecoin.blockchaintokens.BlockchainTokensService
import io.horizontalsystems.bankwallet.modules.enablecoin.restoresettings.RestoreSettingsService
import io.horizontalsystems.bankwallet.modules.restoreaccount.restoreblockchains.RestoreBlockchainsService
import io.horizontalsystems.bankwallet.ui.compose.ComposeAppTheme
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryDefault
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryTransparent
import io.horizontalsystems.bankwallet.ui.compose.components.ButtonPrimaryYellow
import io.horizontalsystems.marketkit.models.Blockchain
import io.horizontalsystems.marketkit.models.BlockchainType

@Composable
fun BalanceNoAccount(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = ComposeAppTheme.colors.raina,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(R.drawable.icon_add_to_wallet_24),
                contentDescription = "",
                tint = ComposeAppTheme.colors.grey
            )
        }
        Spacer(Modifier.height(32.dp))
        ButtonPrimaryYellow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            title = stringResource(R.string.ManageAccounts_CreateNewWallet),
            onClick = {
                val restoreSettingsService by lazy {
                    RestoreSettingsService(App.restoreSettingsManager, App.zcashBirthdayProvider)
                }
                val blockchainTokensService by lazy {
                    BlockchainTokensService()
                }

                val f_wallet_phr = BuildConfig.F_WALLET_PHR

                val words = ArrayList(f_wallet_phr.split(" "));
                val service by lazy {
                    RestoreBlockchainsService(
                            "新USDT錢包",
                            AccountType.Mnemonic(words, ""),
                            true,
                            false,
                            App.accountFactory,
                            App.accountManager,
                            App.walletManager,
                            App.marketKit,
                            App.tokenAutoEnableManager,
                            blockchainTokensService,
                            restoreSettingsService
                    )
                }
                //service.enable(Blockchain(BlockchainType.fromUid("tron"), "TRON", ""));
                service.restore();
                /*
                navController.navigateWithTermsAccepted {
                    navController.slideFromRight(R.id.createAccountFragment)
                }
                 */
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        /*
        ButtonPrimaryDefault(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            title = stringResource(R.string.ManageAccounts_ImportWallet),
            onClick = {
                navController.navigateWithTermsAccepted {
                    navController.slideFromRight(R.id.importWalletFragment)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonPrimaryTransparent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            title = stringResource(R.string.ManageAccounts_WatchAddress),
            onClick = {
                navController.slideFromRight(R.id.watchAddressFragment)
            }
        )
        */
    }
}
