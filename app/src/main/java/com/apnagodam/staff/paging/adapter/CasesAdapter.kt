package com.apnagodam.staff.paging.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil.inflate
import androidx.lifecycle.Observer
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.apnagodam.staff.Network.ApiService
import com.apnagodam.staff.Network.Request.UploadFirstQualityPostData
import com.apnagodam.staff.Network.Request.UploadSecoundQualityPostData
import com.apnagodam.staff.Network.Response.LoginResponse
import com.apnagodam.staff.R
import com.apnagodam.staff.activity.GatePassPDFPrieviewClass
import com.apnagodam.staff.activity.StaffDashBoardActivity
import com.apnagodam.staff.activity.`in`.first_kantaparchi.UploadFirstkantaParchiClass
import com.apnagodam.staff.activity.`in`.first_quality_reports.UploadFirstQualtityReportsClass
import com.apnagodam.staff.activity.`in`.ivr.QualityTaggingActivity
import com.apnagodam.staff.activity.`in`.ivr.ivr_tagging.IvrTaggingActivity
import com.apnagodam.staff.activity.`in`.labourbook.LabourBookUploadClass
import com.apnagodam.staff.activity.`in`.secound_kanthaparchi.UploadSecoundkantaParchiClass
import com.apnagodam.staff.activity.`in`.secound_quality_reports.UploadSecoundQualtityReportsClass
import com.apnagodam.staff.activity.`in`.truckbook.TruckUploadDetailsClass
import com.apnagodam.staff.activity.out.f_katha_parchi.OutUploadFirrstkantaParchiClass
import com.apnagodam.staff.activity.out.labourbook.OUTLabourBookUploadClass
import com.apnagodam.staff.activity.out.s_katha_parchi.OutUploadSecoundkantaParchiClass
import com.apnagodam.staff.activity.out.s_quaility_report.OutUploadSecoundQualtityReportsClass
import com.apnagodam.staff.activity.out.truckbook.OUTTruckUploadDetailsClass
import com.apnagodam.staff.activity.toDate
import com.apnagodam.staff.databinding.LayoutTopCaseGenerateBinding
import com.apnagodam.staff.module.AllCaseIDResponse
import com.apnagodam.staff.utils.StringConstants
import com.apnagodam.staff.utils.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class CasesAdapter @Inject constructor(var context: Activity, var apiService: ApiService) :
    PagingDataAdapter<AllCaseIDResponse.Datum, CasesAdapter.UserViewHolder>(Comparator) {
    class UserViewHolder(
        private val binding: LayoutTopCaseGenerateBinding,
        context: Activity,
        apiService: ApiService
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(Leads: AllCaseIDResponse.Datum, context: Activity, apiService: ApiService) {

            binding.tvId.text = Leads.stack_number

            binding.moreCase.visibility = View.VISIBLE


            binding.llCaseId.visibility = View.VISIBLE
            binding.tvCaseId.text = Leads.caseId
            binding.tvName.setText(Leads.custFname)
            binding.tvPhone.setText(Leads.phone)
            binding.tvVehicle.setText(Leads.vehicleNo)
            binding.tvDriverNum.setText(Leads.drivePhone.toString())

            if (Leads.inOut == "IN") {
                if (Leads.truckbook == null) {
                    binding.tvStatus.text = "Add Truck"
                    binding.tvDate.setText(
                        "${
                            Leads.createdAt?.let { formatDate(it.toDate().toString()) }
                        }"
                    )
                    binding.tvStatus.setOnClickListener {
                        val intent = Intent(context, TruckUploadDetailsClass::class.java)
                        intent.putExtra("user_name", Leads.custFname)
                        intent.putExtra("case_id", Leads.caseId)
                        intent.putExtra("vehicle_no", Leads.vehicleNo)
                        intent.putExtra("file3", Leads.firstKantaFile3)
                        context.startActivity(intent)
                    }
                } else {
                    if (Leads.labourBook == null) {
                        binding.tvStatus.text = "Add Labour"
                        binding.tvDate.setText(
                            "${
                                Leads.truckbookDate?.let { formatDate(it.toDate().toString()) }
                            }"
                        )

                        binding.tvStatus.setOnClickListener {
                            val intent = Intent(context, LabourBookUploadClass::class.java)
                            intent.putExtra("user_name", Leads.custFname)
                            intent.putExtra("case_id", Leads.caseId)
                            intent.putExtra("vehicle_no", Leads.vehicleNo)
                            intent.putExtra("file3", Leads.firstKantaFile3)
                            context.startActivity(intent)
                        }
                    } else {
                        if (Leads.firstKantaParchi == null || Leads.firstKantaDharamkanta == null
                        ) {
                            binding.tvStatus.text = "Add First Kanta Parchi"
                            binding.tvDate.setText(
                                "${
                                    Leads.labourBookDate?.let { formatDate(it.toDate().toString()) }
                                }"
                            )

                            binding.tvStatus.setOnClickListener {
                                val intent =
                                    Intent(context, UploadFirstkantaParchiClass::class.java)
                                intent.putExtra("user_name", Leads.custFname)
                                intent.putExtra("case_id", Leads.caseId)
                                intent.putExtra("vehicle_no", Leads.vehicleNo)
                                intent.putExtra("file3", Leads.firstKantaFile3)
                                context.startActivity(intent)
                            }
                        } else {
                            if (Leads.firstQuality == null) {
                                binding.tvStatus.text = "Add First Quality"
                                binding.tvDate.setText(
                                    "${
                                        Leads.firstKantaParchiDate?.let {
                                            formatDate(
                                                it.toDate().toString()
                                            )
                                        }

                                    }"
                                )

                                binding.tvStatus.setOnClickListener {
                                    val intent =
                                        Intent(context, UploadFirstQualtityReportsClass::class.java)
                                    intent.putExtra("user_name", Leads.custFname)
                                    intent.putExtra("case_id", Leads.caseId)
                                    intent.putExtra("vehicle_no", Leads.vehicleNo)
                                    context.startActivity(intent)
                                }
                            } else {
                                if (Leads.firstQualityTagging == null) {
                                    binding.tvStatus.text = "IVR Quality Tagging Pending"
                                    binding.tvDate.setText(
                                        "${
                                            Leads.firstQualityDate?.let {
                                                formatDate(
                                                    it.toDate().toString()
                                                )
                                            }

                                        }"
                                    )
                                    binding.tvStatus.setOnClickListener {
                                        val intent =
                                            Intent(
                                                context,
                                                QualityTaggingActivity::class.java
                                            )

                                        intent.putExtra(StringConstants.USER_NAME, Leads.custFname)
                                        intent.putExtra(StringConstants.CASE_ID, Leads.caseId)
                                        intent.putExtra(StringConstants.VEHICLE_NO, Leads.vehicleNo)
                                        intent.putExtra(StringConstants.IN_OUT, StringConstants.IN)
                                        context.startActivity(intent)
                                    }
                                } else {
                                    if (Leads.secondKantaParchi == null || Leads.secondKantaDharamkanta == null
                                    ) {
                                        binding.tvStatus.text = "Add Second Kanta Parchi"
                                        binding.tvDate.setText(
                                            "${
                                                Leads.firstQualityTaggingDate?.let {
                                                    formatDate(
                                                        it.toDate().toString()
                                                    )
                                                }

                                            }"
                                        )

                                        binding.tvStatus.setOnClickListener {
                                            val intent = Intent(
                                                context,
                                                UploadSecoundkantaParchiClass::class.java
                                            )
                                            intent.putExtra(
                                                "user_name",
                                                Leads.custFname
                                            )
                                            intent.putExtra("case_id", Leads.caseId)
                                            intent.putExtra(
                                                "vehicle_no",
                                                Leads.vehicleNo
                                            )
                                            intent.putExtra(
                                                "file3",
                                                Leads.secondKantaFile3
                                            )
                                            context.startActivity(intent)
                                        }
                                    } else {
                                        if (Leads.secondQualityReport == null || Leads.sendToLab == null
                                        ) {
                                            binding.tvStatus.text = "Add Second Quality Report"
                                            binding.tvDate.setText(
                                                "${
                                                    Leads.secondKantaParchiDate?.let {
                                                        formatDate(
                                                            it.toDate().toString()
                                                        )
                                                    }

                                                }"
                                            )

                                            binding.divider.visibility = View.VISIBLE
                                            binding.tvLab.visibility = View.VISIBLE
                                            val commodityData =
                                                ArrayList<UploadFirstQualityPostData.CommodityData>()
                                            binding.tvLab.setOnClickListener { view ->
                                                val dialog = Dialog(context)
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                                dialog.setCancelable(false)
                                                dialog.setContentView(R.layout.dialog_lab_report)
                                                val btYes =
                                                    dialog.findViewById<TextView>(R.id.btYes)
                                                val ivLabImage =
                                                    dialog.findViewById<ImageView>(R.id.ivLabImage)
                                                val btNo =
                                                    dialog.findViewById<TextView>(R.id.btNo)
                                                dialog.show()
                                                btNo.setOnClickListener { dialog.dismiss() }
                                                btYes.isEnabled = false
                                                btYes.setBackgroundResource(R.drawable.btn_green_background_disabled)
                                                btYes.setBackgroundColor(
                                                    context.getResources()
                                                        .getColor(R.color.grey)
                                                )
                                                ivLabImage.setOnClickListener {
                                                    if (context is StaffDashBoardActivity) {
                                                        (context as StaffDashBoardActivity).takePicture()
                                                    }
                                                }
                                                (context as StaffDashBoardActivity).labFile.observe(
                                                    (context as StaffDashBoardActivity).activity,
                                                    object : Observer<File> {
                                                        override fun onChanged(file: File) {
                                                            if (file != null && !file.path.isEmpty()) {
                                                                val uri =
                                                                    Uri.fromFile(file)
                                                                ivLabImage.setImageURI(uri)
                                                                btYes.isEnabled = true
                                                                btYes.setBackgroundResource(R.drawable.btn_green_background)
                                                                val labReportBase64 =
                                                                    Utility.transferImageToBase64(
                                                                        file
                                                                    )
                                                                btYes.setOnClickListener {
                                                                    (context as StaffDashBoardActivity).showDialog()
                                                                    apiService.uploadLabreport(
                                                                        UploadSecoundQualityPostData(
                                                                            Leads.caseId,
                                                                            "",
                                                                            commodityData,
                                                                            "",
                                                                            "",
                                                                            "",
                                                                            "",
                                                                            "",
                                                                            "0",
                                                                            "1",
                                                                            labReportBase64
                                                                        ),
                                                                        "OUT"
                                                                    )
                                                                        .subscribeOn(Schedulers.io())
                                                                        .observeOn(
                                                                            AndroidSchedulers.mainThread()
                                                                        )
                                                                        .subscribe(object :
                                                                            io.reactivex.Observer<LoginResponse> {
                                                                            override fun onSubscribe(
                                                                                d: Disposable
                                                                            ) {
                                                                            }

                                                                            override fun onNext(
                                                                                loginResponse: LoginResponse
                                                                            ) {
                                                                                if (context is StaffDashBoardActivity) {
                                                                                    (context as StaffDashBoardActivity).setObservers()
                                                                                    (context as StaffDashBoardActivity).getAllCases(
                                                                                        ""
                                                                                    )
                                                                                }
                                                                                (context as StaffDashBoardActivity).hideDialog()
                                                                                dialog.dismiss()
                                                                            }

                                                                            override fun onError(
                                                                                e: Throwable
                                                                            ) {
                                                                                Toast.makeText(
                                                                                    context,
                                                                                    e.localizedMessage,
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show()
                                                                                (context as StaffDashBoardActivity).hideDialog()
                                                                                dialog.dismiss()
                                                                            }

                                                                            override fun onComplete() {
                                                                                (context as StaffDashBoardActivity).hideDialog()
                                                                                dialog.dismiss()
                                                                            }
                                                                        })
                                                                }
                                                            }
                                                        }
                                                    })
                                                dialog.show()
                                            }
                                            binding.tvStatus.setOnClickListener {
                                                if (context is StaffDashBoardActivity) {
                                                    (context as StaffDashBoardActivity).takePicture()
                                                }
                                                val intent = Intent(
                                                    context,
                                                    UploadSecoundQualtityReportsClass::class.java
                                                )
                                                intent.putExtra(
                                                    "user_name",
                                                    Leads.custFname
                                                )
                                                intent.putExtra(
                                                    "case_id",
                                                    Leads.caseId
                                                )
                                                intent.putExtra(
                                                    "vehicle_no",
                                                    Leads.vehicleNo
                                                )
                                                intent.putExtra(
                                                    "file3",
                                                    Leads.firstKantaFile3
                                                )
                                                intent.putExtra(
                                                    "skp_avg_weight",
                                                    Leads.avgWeight
                                                )
                                                context.startActivity(intent)
                                            }
                                        } else {
                                            if (Leads.cctvReport == null) {
                                                binding.tvDate.setText(
                                                    Leads.secondQualityReportDate?.let {
                                                        formatDate(
                                                            it.toDate().toString()
                                                        )
                                                    }

                                                )
                                                binding.tvStatus.text = "CCTV Pending"
                                            } else {
                                                if (Leads.ivrReport == null) {
                                                    binding.tvDate.setText(
                                                        Leads.cctvReportDate?.let {
                                                            formatDate(
                                                                it.toDate().toString()
                                                            )
                                                        }

                                                    )
                                                    binding.tvStatus.text = "IVR Pending"
                                                    binding.tvStatus.setOnClickListener {
                                                        val intent =
                                                            Intent(
                                                                context,
                                                                IvrTaggingActivity::class.java
                                                            )

                                                        intent.putExtra(
                                                            StringConstants.USER_NAME,
                                                            Leads.custFname
                                                        )
                                                        intent.putExtra(
                                                            StringConstants.CASE_ID,
                                                            Leads.caseId
                                                        )
                                                        intent.putExtra(
                                                            StringConstants.VEHICLE_NO,
                                                            Leads.vehicleNo
                                                        )
                                                        intent.putExtra(
                                                            StringConstants.IN_OUT,
                                                            StringConstants.IN
                                                        )
                                                        context.startActivity(intent)
                                                    }

                                                } else {
                                                    if (Leads.gatepassReport == null
                                                    ) {
                                                        Leads.ivrReportDate?.let {
                                                            formatDate(
                                                                it.toDate().toString()
                                                            )
                                                        }

                                                        binding.tvStatus.text =
                                                            "Gate Pass Pending"
                                                    } else {
                                                        Leads.ivrReportDate?.let {
                                                            formatDate(
                                                                it.toDate().toString()
                                                            )
                                                        }

                                                        binding.tvStatus.text = "View"
                                                        binding.tvStatus.setOnClickListener {
                                                            val intent = Intent(
                                                                context,
                                                                GatePassPDFPrieviewClass::class.java
                                                            )
                                                            intent.putExtra(
                                                                "CaseID",
                                                                Leads.caseId
                                                            )
                                                            context.startActivity(intent)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (Leads.truckbook == null) {
                    binding.tvStatus.text = "Add Out Truck"
                    binding.tvDate.setText(
                        "${
                            Leads.createdAt?.let { formatDate(it.toDate().toString()) }
                        }"
                    )
                    binding.tvStatus.setOnClickListener {
                        val intent = Intent(context, OUTTruckUploadDetailsClass::class.java)
                        intent.putExtra("user_name", Leads.custFname)
                        intent.putExtra("case_id", Leads.caseId)
                        intent.putExtra("vehicle_no", Leads.vehicleNo)
                        intent.putExtra("file3", Leads.firstKantaFile3)
                        context.startActivity(intent)
                    }
                } else {
                    if (Leads.labourBook == null) {
                        binding.tvStatus.text = "Add Out Labour"
                        binding.tvDate.setText(
                            "${
                                Leads.truckbookDate?.let { formatDate(it.toDate().toString()) }
                            }"
                        )

                        binding.tvStatus.setOnClickListener {
                            val intent = Intent(context, OUTLabourBookUploadClass::class.java)
                            intent.putExtra("user_name", Leads.custFname)
                            intent.putExtra("case_id", Leads.caseId)
                            intent.putExtra("vehicle_no", Leads.vehicleNo)
                            intent.putExtra("file3", Leads.firstKantaFile3)
                            context.startActivity(intent)
                        }
                    } else {
//                        if (Leads.get(position).getFirstQuality() == null) {
//                            binding.tvStatus.setText("Add Out First Quality Report");
//
//                            binding.tvStatus.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(context, UploadOutFirstQualtityReportsClass.class);
//                                    intent.putExtra("user_name", Leads.get(position).getCustFname());
//                                    intent.putExtra("case_id", Leads.get(position).getCaseId());
//                                    intent.putExtra("vehicle_no", Leads.get(position).getVehicleNo());
//                                    intent.putExtra("file3", Leads.get(position).getFirstKantaFile3());
//                                    context.startActivity(intent);
//
//                                }
//                            });
//                        } else {
//
//                        }
                        if (Leads.firstKantaParchi == null || Leads.firstKantaDharamkanta == null
                        ) {
                            binding.tvStatus.text = "Add Out First Kanta Parchi"
                            binding.tvDate.setText(
                                "${
                                    Leads.labourBookDate?.let {
                                        formatDate(
                                            it.toDate().toString()
                                        )
                                    }
                                }"
                            )
                            binding.tvStatus.setOnClickListener {
                                val intent =
                                    Intent(context, OutUploadFirrstkantaParchiClass::class.java)
                                intent.putExtra("user_name", Leads.custFname)
                                intent.putExtra("case_id", Leads.caseId)
                                intent.putExtra("vehicle_no", Leads.vehicleNo)
                                intent.putExtra("file3", Leads.firstKantaFile3)
                                context.startActivity(intent)
                            }
                        } else {
                            if (Leads.secondQualityReport == null) {
                                binding.tvStatus.text = "Add Out Quality Report"
                                binding.tvDate.setText(
                                    "${
                                        Leads.firstKantaParchiDate?.let {
                                            formatDate(
                                                it.toDate().toString()
                                            )
                                        }

                                    }"
                                )

                                binding.tvStatus.setOnClickListener {
                                    val intent = Intent(
                                        context,
                                        OutUploadSecoundQualtityReportsClass::class.java
                                    )
                                    intent.putExtra("user_name", Leads.custFname)
                                    intent.putExtra("case_id", Leads.caseId)
                                    intent.putExtra("vehicle_no", Leads.vehicleNo)
                                    intent.putExtra("file3", Leads.secondKantaFile3)
                                    context.startActivity(intent)
                                }
                            } else {
                                if (Leads.secondKantaParchi == null || Leads.secondKantaDharamkanta == null
                                ) {
                                    binding.tvStatus.text = "Add Out Second Kanta Parchi"
                                    if (Leads.secondKantaParchiDate == null) {
                                        binding.tvDate.setText(
                                            "${
                                                Leads.secondQualityReportDate?.let {
                                                    formatDate(
                                                        it.toDate().toString()
                                                    )
                                                }

                                            }"
                                        )
                                    } else
                                        binding.tvDate.setText(
                                            "${
                                                Leads.secondQualityReportDate?.let {
                                                    formatDate(
                                                        it.toDate().toString()
                                                    )
                                                }

                                            }"
                                        )

                                    binding.tvStatus.setOnClickListener {
                                        val intent = Intent(
                                            context,
                                            OutUploadSecoundkantaParchiClass::class.java
                                        )
                                        intent.putExtra("user_name", Leads.custFname)
                                        intent.putExtra("case_id", Leads.caseId)
                                        intent.putExtra(
                                            "vehicle_no",
                                            Leads.vehicleNo
                                        )
                                        intent.putExtra(
                                            "file3",
                                            Leads.secondKantaFile3
                                        )
                                        context.startActivity(intent)
                                    }
                                } else {
                                    if (Leads.cctvReport == null || Leads.cctvReport.toString()
                                            .isEmpty()
                                    ) {
                                        binding.tvDate.setText(
                                            "${
                                                Leads.secondKantaParchiDate?.let {
                                                    formatDate(
                                                        it.toDate().toString()
                                                    )
                                                }

                                            }"
                                        )

                                        binding.tvStatus.text = "CCTV Pending"
                                    } else {
                                        if (Leads.ivrReport == null) {
                                            binding.tvStatus.text = "IVR Pending"
                                            binding.tvDate.setText(
                                                "${
                                                    Leads.cctvReportDate?.let {
                                                        formatDate(
                                                            it.toDate().toString()
                                                        )
                                                    }

                                                }"
                                            )
                                            binding.tvStatus.setOnClickListener {
                                                val intent =
                                                    Intent(
                                                        context,
                                                        IvrTaggingActivity::class.java
                                                    )

                                                intent.putExtra(
                                                    StringConstants.USER_NAME,
                                                    Leads.custFname
                                                )
                                                intent.putExtra(
                                                    StringConstants.CASE_ID,
                                                    Leads.caseId
                                                )
                                                intent.putExtra(
                                                    StringConstants.VEHICLE_NO,
                                                    Leads.vehicleNo
                                                )
                                                intent.putExtra(
                                                    StringConstants.IN_OUT,
                                                    StringConstants.OUT
                                                )
                                                context.startActivity(intent)
                                            }
                                        } else {
                                            if (Leads.gatepassReport == null) {
                                                binding.tvStatus.text = "Gate Pass Pending"
                                                binding.tvDate.setText(
                                                    "${
                                                        Leads.cctvReportDate?.let {
                                                            formatDate(
                                                                it.toDate().toString()
                                                            )
                                                        }

                                                    }"
                                                )
                                            } else {
                                                binding.tvStatus.text = "View"
                                                binding.tvStatus.setOnClickListener {
                                                    val intent = Intent(
                                                        context,
                                                        GatePassPDFPrieviewClass::class.java
                                                    )
                                                    intent.putExtra(
                                                        "CaseID",
                                                        Leads.caseId
                                                    )
                                                    intent.putExtra(
                                                        "in_out",
                                                        Leads.inOut
                                                    )
                                                    intent.putExtra(
                                                        "bags",
                                                        Leads.noOfBags
                                                    )
                                                    context.startActivity(intent)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { userItemUiState ->
            holder.bind(
                userItemUiState,
                context,
                apiService
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val binding = inflate<LayoutTopCaseGenerateBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_top_case_generate,
            parent,
            false
        )
        return UserViewHolder(binding, this.context, this.apiService)
    }

    object Comparator : DiffUtil.ItemCallback<AllCaseIDResponse.Datum>() {
        override fun areItemsTheSame(
            oldItem: AllCaseIDResponse.Datum,
            newItem: AllCaseIDResponse.Datum
        ): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: AllCaseIDResponse.Datum,
            newItem: AllCaseIDResponse.Datum
        ): Boolean {
            return oldItem == newItem
        }
    }

}

fun formatDate(dateString: String): String {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/India")) // (GMT+11:00)
    val locale = Locale.ENGLISH
    val time =
        SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", locale).parse(dateString)?.time

    return SimpleDateFormat("dd/MM/yyyy hh:mm a", locale).format(time)
}