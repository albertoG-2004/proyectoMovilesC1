import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviles.ui.register.data.RegisterModel
import com.example.moviles.ui.register.ui.RegisterViewModel
import com.example.moviles.ui.register.ui.RegisterViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviles.apiService.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RegisterScreen(
    navigateToLogin: () -> Unit
){
    val apiService = Retrofit.Builder()
        .baseUrl("http://192.168.56.1:3000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    val registerModel = RegisterModel(apiService)

    val registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(registerModel)
    )

    val navigateToLoginState by registerViewModel.navigateToLogin.collectAsState()
    if (navigateToLoginState){
        LaunchedEffect(Unit){
            navigateToLogin()
        }
    }

    val loading = registerViewModel.loading
    val showMessageError = registerViewModel.showError
    val messageError = registerViewModel.messageError

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = registerViewModel.name,
            onValueChange = {registerViewModel.onNameChanged(it)},
            label = { Text("Nombre") },
            isError = registerViewModel.errorName != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (registerViewModel.errorName != null){
            Text(
                text = registerViewModel.errorName!!,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = registerViewModel.email,
            onValueChange = { registerViewModel.onEmailChanged(it) },
            label = { Text("Email") },
            isError = registerViewModel.errorEmail != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (registerViewModel.errorEmail != null){
            Text(
                text = registerViewModel.errorEmail!!,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = registerViewModel.password,
            onValueChange = {registerViewModel.onPasswordChanged(it)},
            label = { Text("Contraseña") },
            isError = registerViewModel.errorPassword != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (registerViewModel.errorPassword != null){
            Text(
                text = registerViewModel.errorPassword!!,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = registerViewModel.confirmPassword,
            onValueChange = {registerViewModel.onConfirmPasswordChanged(it)},
            label = { Text("Confirmar contraseña") },
            isError = registerViewModel.errorConfirmPassword != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (registerViewModel.errorConfirmPassword != null){
            Text(
                text = registerViewModel.errorConfirmPassword!!,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {registerViewModel.onRegisterButtonClicked()},
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading
        ) {
            if (loading){
                Text(text = "Cargando...")
            }else{
                Text(text = "Registrarse")
            }

        }
        if (showMessageError){
            Text(
                text = messageError,
                color = Color.Red,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(navigateToLogin = {})
}