package com.apnagodam.staff.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.marginLeft
import androidx.core.view.setMargins
import com.apnagodam.staff.Base.BaseActivity
import com.apnagodam.staff.Network.NetworkCallbackWProgress
import com.apnagodam.staff.Network.NetworkResult
import com.apnagodam.staff.Network.viewmodel.GatePassViewModel
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityGatePassPdfFileBinding
import com.apnagodam.staff.module.GatePassPDFPojo
import com.apnagodam.staff.utils.Utility
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class GatePassPDFPrieviewClass : BaseActivity<ActivityGatePassPdfFileBinding?>() {
    private var bitmap: Bitmap? = null
    private var caseiD: String? = ""
    val gatePassViewModel by viewModels<GatePassViewModel>()
    var inOut: String = "IN"
    override fun getLayoutResId(): Int {
        return R.layout.activity_gate_pass_pdf_file
    }

    override fun setUp() {
        val intent = intent
        caseiD = intent.extras!!.getString("CaseID")
        inOut = intent.extras!!.getString("in_out").toString()
        showDialog()
        gatePassViewModel.getGatePassPdf(caseiD.toString())
        binding!!.ivClose.setOnClickListener {
            finish()
        }

        if (inOut == "OUT") {
            binding!!.llTraupline.visibility = View.GONE
        }
        gatePassViewModel.gatePassPdfResponse.observe(this) {
            when (it) {
                is NetworkResult.Error -> {
                    hideDialog()
                }

                is NetworkResult.Loading -> {

                }

                is NetworkResult.Success -> {
                    hideDialog()
                    if (it.data != null) {
                        if (it.data.data.status == 1) {
                            it.data.let { body ->
                                try {
                                    binding!!.gatepassNo.text = "No :" + body.data.gatePass
                                    binding!!.date.text = "Date :" + body.data.gatepassDate
                                    binding!!.terminalName.text = body.data.terminalName
                                    binding!!.caseId.text = body.data.caseId
                                    binding!!.buyerName.text = body.data.custFname
                                    binding!!.kathaParchiNo.text = "" + body.data.kantaParchiNo
                                    binding!!.truckNo.text = body.data.vehicleNo
                                    binding!!.dharmKathaName.text = body.data.dharamKantaName
                                    binding!!.stackNo.text = body.data.stackNo
                                    binding!!.bags.text = "${body.data.noOfBags} ( ${body.data.noOfBags} / ${body.data.displacedBags})"
                                    binding!!.avgWweight.text = body.data.totalWeight + " " + "QTL."
                                    binding!!.commodityName.text = body.data.commodityName
                                    binding!!.transporterName.text = body.data.transporterName
                                    binding!!.tansporterMobileNo.text = body.data.transporterPhoneNo
                                    if(body.data.contractorId.toString().toInt()>1){
                                        binding!!.labourContractor.text = "Company"

                                    }
                                    else{
                                        binding!!.labourContractor.text = "Client"

                                    }
                                    if(body.data.transporterId.toString().toInt()>1){
                                        binding!!.transporterName.text = "Company"

                                    }
                                    else{
                                        binding!!.transporterName.text = "Client"

                                    }

                                    var listOfQuality  = arrayListOf<String>();
                                    listOfQuality.add("FF")
                                    listOfQuality.add("GG")
                                    listOfQuality.add("Moisture")
                                    var layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
                                    layoutParams.setMargins(10,5,10,5)
                                    for (i  in body.data.qvList){
                                        var textView = TextView(this)
                                        textView.layoutParams =  layoutParams
                                        textView.setBackgroundResource(R.drawable.btn_borders_gatepass)
                                        textView.setPadding(10,5,10,5)
                                        textView.setTextSize(10f)
                                        textView.setText("${i.name} : ${i.value}")

                                        binding!!.llQuality.addView(textView)

                                    }
                                    /** */
                                    if (body.data.truckFacility != null) {
                                        binding!!.facilityTruck.text =
                                            if (body.data.truckFacility.toInt() == 1) "YES" else "No"
                                    } else if (body.data.truckFacility != null) {
                                        binding!!.facilityBardhna.text =
                                            if (body.data.bagsFacility.toInt() == 1) "YES" else "No"
                                    }
                                    binding!!.oldKathaParchiNo.text = body.data.oldKantaParchi
                                    binding!!.oldKathaParchiTotalWeight.text =
                                        body.data.oldTotalWeight
                                    binding!!.oldKathaParchiAvgWeight.text =
                                        body.data.oldOriginalWeight
                                    binding!!.kathaName.text = body.data.oldKantaName
                                    /** */
                                    binding!!.driverName.text =
                                        "वरिष्ठ अधिकारी : " + body.data.gatePassCdfUserName
                                    binding!!.driverMobile.text =
                                        "चालक का मोबाइल न.: " + body.data.driverPhone
                                    binding!!.whsMobile.text =
                                        "सुपरवाइजर : " +  body.data.firstName + " " + body.data.lastName+"(${body.data.supervisorEmpId})"
                                    binding!!.whsName.text =
                                        "वरिष्ठ अधिकारी : " + body.data.empFirstName + " " + body.data.empLastName+"(${body.data.employeeEmpId})"

                                    binding!!.comment.text = body.data.notes
                                    hideDialog()
                                } catch (e: Exception) {
                                    e.stackTrace
                                }
                            }
                        }
                    }
                }
            }
        }


        binding!!.tvDone.setOnClickListener {
            Log.d("size", " " + binding!!.scrollLL.width + "  " + binding!!.scrollLL.width)
            bitmap = loadBitmapFromView(
                binding!!.scrollLL,
                binding!!.scrollLL.width,
                binding!!.scrollLL.height
            )
            nextMClass()
            // createPdf();
        }
    }

    private fun createPdf() {
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        //  Display display = wm.getDefaultDisplay();
        val displaymetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val hight = 3508f
        val width = 2500f /*displaymetrics.widthPixels*/
        val convertHighet = hight.toInt()
        val convertWidth = width.toInt()
        //        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);
        val document = PdfDocument()
        val pageInfo = PageInfo.Builder(convertWidth, convertHighet, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        canvas.drawPaint(paint)
        bitmap = Bitmap.createScaledBitmap(bitmap!!, convertWidth, convertHighet, true)
        paint.color = Color.BLUE
        canvas.drawBitmap(bitmap!!, 0f, 0f, null)
        document.finishPage(page)

        // write the document content
        /*  String targetPdf = "/sdcard/pdffromScroll.pdf";
        File filePath;
        filePath = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/sdcard/pdffromScroll.pdf");*/
        var camFile = Utility.getOutpuPDFtMediaFile(this, "pdf")
        if (camFile.exists()) {
            camFile.delete()
        }
        camFile = Utility.getOutpuPDFtMediaFile(this, "pdf")
        try {
            document.writeTo(FileOutputStream(camFile))
            showToast("file saved successfully!")
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Something wrong: $e", Toast.LENGTH_LONG).show()
        }

        // close the document
        document.close()
        //  Toast.makeText(this, "PDF of Scroll is created!!!", Toast.LENGTH_SHORT).show();
        //   openGeneratedPDF(camFile)
    }

    private fun requestPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writeExternalPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readExternalPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalPermission == PackageManager.PERMISSION_GRANTED && readExternalPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CAMERA_CODE -> if (grantResults.size > 0) {
                var i = 0
                while (i < grantResults.size) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions()
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permissions[i]
                            )
                        ) return
                    } else {
                        createPdf()
                    }
                    i++
                }
                // open dialog
            }
        }
    }

    private fun openGeneratedPDF(camFile: File) {
        // File camFile = Utility.getOutpuPDFtMediaFile(this, "pdf");
        /* if (camFile.exists()) {
            camFile.delete();
        }
        camFile = Utility.getOutpuPDFtMediaFile(this, "pdf");*/
        val uri =
            FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", camFile)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        // intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val targetIntent = Intent.createChooser(intent, "Open File")
        try {
            startActivity(targetIntent)
            //  finish();
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this@GatePassPDFPrieviewClass,
                "No Application available to view pdf",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun nextMClass() {
        if (requestPermissions()) {
            createPdf()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_CAMERA_CODE
            )
        }
    }


    companion object {
        private const val REQUEST_CAMERA_CODE = 120
        fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap {
            val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.draw(c)
            return b
        }
    }
}
