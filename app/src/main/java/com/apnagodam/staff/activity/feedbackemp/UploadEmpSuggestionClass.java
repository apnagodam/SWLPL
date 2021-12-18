package com.apnagodam.staff.activity.feedbackemp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.EmpFeedbackRequest;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.StaffDashBoardActivity;
import com.apnagodam.staff.adapter.EmpSuggestionListAdpter;
import com.apnagodam.staff.databinding.EmpFeedbackBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.module.FeedbackListPojo;
import com.apnagodam.staff.module.FeedbackPojo;
import com.apnagodam.staff.utils.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class UploadEmpSuggestionClass extends BaseActivity<EmpFeedbackBinding> {
    /*error log  List */
    List<String> ErrorName;
    ArrayAdapter<String> SpinnererrorAdapter;
    String SelectedErrorIDIs = null;
    List<FeedbackPojo.Datum> data;
    private EmpSuggestionListAdpter empSuggestionListAdpter;
    private List<FeedbackListPojo.Datum> getOrdersList;
    private List<FeedbackListPojo.Datum> tempraryList;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private static final int REQUEST_CODE_SPEECH_INPUT_SOLUTION = 2;

    @Override
    protected int getLayoutResId() {
        return R.layout.emp_feedback;
    }

    @Override
    protected void setUp() {
        ErrorName = new ArrayList<>();
        getOrdersList = new ArrayList();
        tempraryList = new ArrayList();
        ErrorName.add("Select Department");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // get approve person  list
        setDataAdapter();

        /*speech to text */
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }


        getBidsList();
        apiService.getDepartmentList().enqueue(new NetworkCallback<FeedbackPojo>(getActivity()) {
            @Override
            protected void onSuccess(FeedbackPojo body) {
                data = body.getData();
                for (int i = 0; i < body.getData().size(); i++) {
                    ErrorName.add(body.getData().get(i).getDesignation());
                }

            }
        });
        clickListner();
        SpinnererrorAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, ErrorName) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                return v;
            }
        };
        SpinnererrorAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerdepartmentName.setAdapter(SpinnererrorAdapter);
        binding.spinnerdepartmentName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    SelectedErrorIDIs = parent.getItemAtPosition(position).toString();
                    for (int i = 0; i < ErrorName.size(); i++) {
                        if (SelectedErrorIDIs.equalsIgnoreCase(data.get(i).getDesignation())) {
                            SelectedErrorIDIs = "" + data.get(i).getId();
                            break;
                        }
                    }
                } else {
                    SelectedErrorIDIs = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // can leave this empty
            }
        });
        binding.speckproblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
               /* intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");*/
                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException  e) {
                    Toast.makeText(UploadEmpSuggestionClass.this, "Sorry your device not supported" , Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.specksolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT_SOLUTION);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(UploadEmpSuggestionClass.this, "Sorry your device not supported", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},REQUEST_CODE_SPEECH_INPUT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED);
             //   Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                binding.probleam.setText(Objects.requireNonNull(result).get(0));
            }
        } else {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                binding.solution.setText(Objects.requireNonNull(result).get(0));
            }
        }
    }

    private void getBidsList() {
        apiService.getAllFeedbackList().enqueue(new NetworkCallback<FeedbackListPojo>(getActivity()) {
            @Override
            protected void onSuccess(FeedbackListPojo body) {
                tempraryList.clear();
                getOrdersList.clear();
                binding.approvalRequest.setText("Approval Request " + "(" + "" + ((body.getReqCount()) != null ? body.getReqCount() : "0") + ")");
                if (body.getData().size() == 0) {
                    binding.txtemptyMsg.setVisibility(View.VISIBLE);
                    binding.gatepassList.setVisibility(View.GONE);
                } else {
                    binding.txtemptyMsg.setVisibility(View.GONE);
                    binding.gatepassList.setVisibility(View.VISIBLE);
                    getOrdersList.clear();
                    tempraryList.clear();
                    getOrdersList.addAll(body.getData());
                    tempraryList.addAll(getOrdersList);
                    empSuggestionListAdpter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setDataAdapter() {
        binding.gatepassList.addItemDecoration(new DividerItemDecoration(UploadEmpSuggestionClass.this, LinearLayoutManager.HORIZONTAL));
        binding.gatepassList.setHasFixedSize(true);
        binding.gatepassList.setNestedScrollingEnabled(false);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(UploadEmpSuggestionClass.this, LinearLayoutManager.VERTICAL, false);
        binding.gatepassList.setLayoutManager(horizontalLayoutManager);
        empSuggestionListAdpter = new EmpSuggestionListAdpter(getOrdersList, UploadEmpSuggestionClass.this, getActivity());
        binding.gatepassList.setAdapter(empSuggestionListAdpter);
    }

    private void clickListner() {
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityAndClear(StaffDashBoardActivity.class);
            }
        });
        binding.approvalRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < SharedPreferencesRepository.getDataManagerInstance().getUserPermission().size(); i++) {
                    if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getPermissionId().equalsIgnoreCase("96")) {
                        if (SharedPreferencesRepository.getDataManagerInstance().getUserPermission().get(i).getEdit() == 1) {
                            startActivity(ApprovalRequestFeedbackListClass.class);
                        }
                    }
                }
            }
        });

        binding.nextsumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showDecisionDialog(UploadEmpSuggestionClass.this, getString(R.string.alert), "Are You Sure to Summit?", new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        if (SelectedErrorIDIs == null) {
                            Toast.makeText(getApplicationContext(), "Select Department Name ", Toast.LENGTH_LONG).show();
                        } else if (TextUtils.isEmpty(stringFromView(binding.probleam))) {
                            Toast.makeText(getApplicationContext(), "Enter Department Problem ", Toast.LENGTH_LONG).show();
                        } else if (TextUtils.isEmpty(stringFromView(binding.solution))) {
                            Toast.makeText(getApplicationContext(), "Enter Department Problem solution", Toast.LENGTH_LONG).show();
                        } else {
                            onNext();
                        }
                    }
                });
            }
        });
    }

    // update file
    public void onNext() {
        apiService.doCreateEmpSuggestion(new EmpFeedbackRequest(SelectedErrorIDIs, stringFromView(binding.probleam), stringFromView(binding.solution))).enqueue(new NetworkCallback<LoginResponse>(getActivity()) {
            @Override
            protected void onSuccess(LoginResponse body) {
                Utility.showAlertDialog(UploadEmpSuggestionClass.this, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                    @Override
                    public void callback() {
                        getBidsList();
                    }
                });
            }
        });
    }

    public void ViewData(int position) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(UploadEmpSuggestionClass.this, R.style.CustomAlertDialog);
        LayoutInflater inflater = UploadEmpSuggestionClass.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.feedback_view, null);
     /*   dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));*/
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        ImageView cancel_btn = dialogView.findViewById(R.id.cancel_btn);
        TextView lead_id = dialogView.findViewById(R.id.lead_id);
        TextView genrated_by = dialogView.findViewById(R.id.genrated_by);
        TextView customer_name = dialogView.findViewById(R.id.customer_name);
        TextView location_title = dialogView.findViewById(R.id.location_title);
        TextView phone_no = dialogView.findViewById(R.id.phone_no);
        TextView commodity_name = dialogView.findViewById(R.id.commodity_name);


        customer_name.setText("" + ((getOrdersList.get(position).getProcessSolution()) != null ? getOrdersList.get(position).getProcessSolution() : "N/A"));
        lead_id.setText("" + ((getOrdersList.get(position).getDesignation()) != null ? getOrdersList.get(position).getDesignation() : "N/A"));
        genrated_by.setText("" + ((getOrdersList.get(position).getProcessProblem()) != null ? getOrdersList.get(position).getProcessProblem() : "N/A"));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = inputFormatter.parse((getOrdersList.get(position).getUpdatedAt()));
                DateFormat outputFormatter = new SimpleDateFormat("dd-MM-yyyy");
                String output = outputFormatter.format(date); // Output : 01/20/2012
                location_title.setText("" + ((getOrdersList.get(position).getUpdatedAt()) != null ? output : "N/A"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        phone_no.setText("" + ((getOrdersList.get(position).getApproveBy()) != null ? getOrdersList.get(position).getApproveBy() : "N/A"));
        if (getOrdersList.get(position).getStatus() == 1) {
            // show button self rejected
            commodity_name.setText(getResources().getString(R.string.pending));
            commodity_name.setTextColor(getResources().getColor(R.color.darkYellow));
        } else if (getOrdersList.get(position).getStatus() == 0) {
            // rejected by self or approved from high authority
            commodity_name.setText(getResources().getString(R.string.rejected));
            commodity_name.setTextColor(getResources().getColor(R.color.red));
        } else if (getOrdersList.get(position).getStatus() == 2) {
            //approved from high authority
            commodity_name.setText(getResources().getString(R.string.approved));
            commodity_name.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
