package com.moonly.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.moonly.app.ui.screens.auth.login.LoginScreen
import com.moonly.app.ui.screens.auth.recovery.PasswordRecoveryScreen
import com.moonly.app.ui.screens.auth.register.RegisterScreen
import com.moonly.app.ui.screens.auth.reset.ResetPasswordScreen
import com.moonly.app.ui.screens.auth.verification.EmailVerificationScreen
import com.moonly.app.ui.screens.onboarding.OnboardingScreen
import com.moonly.app.ui.screens.home.HomeScreen
import com.moonly.app.ui.screens.home.EditPeriodScreen
import com.moonly.app.ui.screens.profile.ProfileMenuScreen
import com.moonly.app.ui.screens.profile.settings.ProfileSettingsScreen
import com.moonly.app.ui.screens.profile.contraceptives.ContraceptivesScreen
import com.moonly.app.ui.screens.profile.medical.MedicalConditionsScreen
import com.moonly.app.ui.screens.profile.caremethod.CareMethodsScreen
import com.moonly.app.ui.screens.splash.SplashScreen
import com.moonly.app.ui.screens.splash.SplashViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ==================== SPLASH ====================
        composable(Screen.Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel()
            val navigateTo by viewModel.navigateTo.collectAsState()

            SplashScreen(
                navigateTo = navigateTo,
                onNavigationHandled = { destination ->
                    navController.navigate(destination) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // ==================== AUTH ====================
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToPasswordRecovery = {
                    navController.navigate(Screen.PasswordRecovery.route)
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToEmailVerification = { // ⚠️ NUEVO
                    navController.navigate(Screen.EmailVerification.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.EmailVerification.route) {
            EmailVerificationScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.EmailVerification.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.EmailVerification.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.PasswordRecovery.route) {
            PasswordRecoveryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ⚠️ NUEVO: Pantalla de reset password con deep link
        composable(
            route = "reset_password/{token}",
            arguments = listOf(
                navArgument("token") {
                    type = NavType.StringType
                    nullable = true
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "moonly://reset-password?token={token}"
                },
                navDeepLink {
                    uriPattern = "https://moonly.app/reset-password?token={token}"
                }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            ResetPasswordScreen(
                token = token,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ==================== ONBOARDING ====================
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // ==================== HOME ====================
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToEditPeriod = {
                    navController.navigate(Screen.EditPeriod.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        // ==================== EDIT PERIOD ====================
        composable(Screen.EditPeriod.route) {
            EditPeriodScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ==================== PROFILE ====================
        composable(Screen.Profile.route) {
            ProfileMenuScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.ProfileSettings.route)
                },
                onNavigateToContraceptives = {
                    navController.navigate(Screen.ProfileContraceptives.route)
                },
                onNavigateToMedicalConditions = {
                    navController.navigate(Screen.ProfileMedicalConditions.route)
                },
                onNavigateToCareMethods = {
                    navController.navigate(Screen.ProfileCareMethods.route)
                }
            )
        }

        composable(Screen.ProfileSettings.route) {
            ProfileSettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ProfileContraceptives.route) {
            ContraceptivesScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ProfileMedicalConditions.route) {
            MedicalConditionsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ProfileCareMethods.route) {
            CareMethodsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}