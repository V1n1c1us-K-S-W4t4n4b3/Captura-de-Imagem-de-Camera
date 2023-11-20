package com.kzdev.capturadeimagemdecamera

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.kzdev.capturadeimagemdecamera.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private var uriImage: Uri? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                this, CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicita permissões
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    CAMERA, WRITE_EXTERNAL_STORAGE
                ),
                11
            )
        } else {  // Permissões concedidas, execute seu código aqui
            try {
                RESULT_OK
                // Seu código aqui para trabalhar com a câmera ou o armazenamento externo
                // Exemplo: chame uma função que capture uma foto ou realize alguma operação
            } catch (ex: Exception) {
                // Ocorreu um erro ao criar o arquivo
                //displayMessage(baseContext, ex.message.toString())
            }
        }


        binding.btCamera.setOnClickListener { obterImagemCamera() }


    }

    fun obterImagemCamera() {

        // variavel q recebe a intenção de açao de captura de imagem
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        //se versao operador foir maior o uigual a versao tiramisu (sdk33) ele opera a forma mais recente
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // versoes mais novas

            val contentValues = ContentValues()

            // configuração de tipo de item e nome, no caso imagem
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "/cameraImage")

            val resolver = contentResolver

            // atribuindo valor para variavel uriImage
            uriImage = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            // intensao de ler e de escrever a imagem
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        } else {  // mesmo procedimento, porem para versoes mais antigas

            val autorizacao = "com.kzdev.capturadeimagemdecamera"
            val diretorio =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val nomeImagem = diretorio.path + "/CAMERAImage" + System.currentTimeMillis() + ".jpg"
            val file = File(nomeImagem)
            uriImage = FileProvider.getUriForFile(baseContext, autorizacao, file)

        }

        // passando a imagem para o destino final
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage)
        startActivityForResult(intent, 22)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == 22 && uriImage != null) { // camera

                //n contei a variavel  val uriImage = data?.data pq ele n resgata do data
                binding.image.setImageURI(uriImage)

            }


        } else {

            val dpValue = 300 // Valor em pixels
            val density = resources.displayMetrics.density
            val dp = (dpValue * density).toInt()


        }
    }
}







