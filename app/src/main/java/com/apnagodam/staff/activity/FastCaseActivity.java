package com.apnagodam.staff.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.Network.NetworkCallback;
import com.apnagodam.staff.Network.Request.LoginPostData;
import com.apnagodam.staff.Network.Request.RequestOfflineCaseData;
import com.apnagodam.staff.Network.Response.BaseResponse;
import com.apnagodam.staff.Network.Response.LoginResponse;
import com.apnagodam.staff.Network.Response.ResponseSendOtp;
import com.apnagodam.staff.Network.Response.ResponseStackData;
import com.apnagodam.staff.Network.Response.ResponseUserData;
import com.apnagodam.staff.Network.Response.ResponseWarehouse;
import com.apnagodam.staff.Network.Response.VerifyOtpFastcase;
import com.apnagodam.staff.R;
import com.apnagodam.staff.activity.caseid.CaseIDGenerateClass;
import com.apnagodam.staff.activity.caseid.CaseListingActivity;
import com.apnagodam.staff.databinding.ActivityFastCaseBinding;
import com.apnagodam.staff.db.SharedPreferencesRepository;
import com.apnagodam.staff.utils.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FastCaseActivity extends BaseActivity<ActivityFastCaseBinding> {

    private Context mContext;
    private ArrayAdapter<String> SpinnerCommudityAdapter, SpinnerTerminalAdapter, SpinnerStackAdapter, SpinnerKantaAdapter, SpinnerContractorAdapter;
    private List<String> StackName, CommudityName, terminalName, kantaName, labourContractorName;
    private List<ResponseWarehouse.CommodityList> commudityData = new ArrayList<>();
    private List<ResponseWarehouse.WarehouseData> terminalData = new ArrayList<>();
    private List<ResponseStackData.Data> stackData = new ArrayList<>();
    private List<ResponseWarehouse.DharmKanta> kantaData = new ArrayList<>();
    private List<ResponseWarehouse.ContractorList> contractorList = new ArrayList<>();

    private String stackID = "", commudityID = "", terminalID = "", kantaID = "", labourContractorID = "";

    private File fileTruck, fileKanta;
    private String IMAGE = "";
    private ResponseUserData userData = new ResponseUserData();
    private static final int REQUEST_CAMERA_CODE = 120;
    private Uri camUri;
    private String VERIFY = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (savedInstanceState != null) {
//            IMAGE = savedInstanceState.getString("param");
//        }

        if (savedInstanceState != null) {
            //    if(imageUri != null){
            VERIFY = savedInstanceState.getString("VERIFY");
            camUri = savedInstanceState.getParcelable("imageUri");
            binding.imgTruck.setImageURI(camUri);

            //     }
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fast_case;
    }

    @Override
    protected void setUp() {

        mContext = FastCaseActivity.this;

        CommudityName = new ArrayList<>();
        terminalName = new ArrayList<>();
        StackName = new ArrayList<>();
        kantaName = new ArrayList<>();
        labourContractorName = new ArrayList<>();

        CommudityName.add(getResources().getString(R.string.commodity_name));
        terminalName.add(getResources().getString(R.string.terminal_name1));
        kantaName.add(getResources().getString(R.string.kanta_name));
        StackName.add(getResources().getString(R.string.select_stack));
        labourContractorName.add(getResources().getString(R.string.select_contractor));

        getWarehouse();

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                if (binding.rbOnline.getId() == checkedId) {

                    binding.lytOtp.setVisibility(View.GONE);
                    binding.btnSendOtp.setVisibility(View.VISIBLE);
                    binding.edtEnterOtp.setVisibility(View.VISIBLE);

                } else if (binding.rbOffline.getId() == checkedId) {

                    binding.lytOtp.setVisibility(View.VISIBLE);
                    binding.edtEnterOtp.setVisibility(View.GONE);
                    binding.btnSendOtp.setVisibility(View.GONE);
                    binding.lytVerifyOtp.setVisibility(View.GONE);

                }
            }
        });

        binding.btnSubmitNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.etPhone.getText().toString().length() == 0) {
                    Toast.makeText(mContext, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                } else if (binding.etPhone.getText().toString().length() < 10) {
                    Toast.makeText(mContext, "Enter Correct Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                    getUserData(binding.etPhone.getText().toString());
                }
            }
        });

        binding.btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendOtpFastCase(binding.etToken.getText().toString());

            }
        });

        binding.btnVerifySendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.edtEnterOtp.getText().toString().length() < 6) {
                    binding.edtEnterOtp.setError("Enter Otp");
                    Toast.makeText(mContext, "Enter Otp", Toast.LENGTH_SHORT).show();
                } else {
                    verifyOtpFastCase(binding.etToken.getText().toString(), binding.edtEnterOtp.getText().toString());
                }

            }
        });

        binding.btnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendOtpFastCase(binding.etToken.getText().toString());

            }
        });

        binding.lytTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IMAGE = "TRUCK";
                onImageClicked();
                //        dispatchTakePictureIntent();
            }
        });

        binding.lytFirstKantaParchi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IMAGE = "KANTA";
                onImageClicked();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.rbOnline.isChecked()) {

                    if (binding.etToken.getText().length() == 0) {
                        Toast.makeText(mContext, "Enter Token Number", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtEnterOtp.getText().length() == 0) {
                        Toast.makeText(mContext, "Enter OTP", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtUsername.getText().length() == 0) {
                        Toast.makeText(mContext, "Check Token Number", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinnerCommudity.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "Select Commodity", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinnerTerminal.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "Select Terminal", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinnerStack.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "Select Stack", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtBag.getText().length() == 0) {
                        Toast.makeText(mContext, "Enter Bags", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtWeight.getText().length() == 0) {
                        Toast.makeText(mContext, "Enter Weight", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtVehicleNo.getText().length() == 0) {
                        Toast.makeText(mContext, "Enter Vehicle No.", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinnerKanta.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "Select Kanta", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtKantaParchiNo.getText().length() == 0) {
                        Toast.makeText(mContext, "Enter Kanta Parchi Number", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinnerLabourContractor.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "Select Contractor", Toast.LENGTH_SHORT).show();
                    } else if (fileTruck == null) {
                        Toast.makeText(mContext, "Select Truck Photo", Toast.LENGTH_SHORT).show();
                    } else if (fileKanta == null) {
                        Toast.makeText(mContext, "Select Kanta Parchi", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinnerSplitDelivery.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "Select Split Delivery", Toast.LENGTH_SHORT).show();
                    } else if (!VERIFY.equals("1")) {
                        Toast.makeText(mContext, "Verify OTP", Toast.LENGTH_SHORT).show();
                    } else {
                        onlineFastCase();
                    }

                } else if (binding.rbOffline.isChecked()) {

                    if (binding.etPhone.getText().length() == 0) {
                        Toast.makeText(mContext, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                    } else if (binding.etToken.getText().length() == 0) {
                        Toast.makeText(mContext, "Enter Token Number", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtUsername.getText().length() == 0) {
                        Toast.makeText(mContext, "Check Phone Number", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinnerCommudity.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "Select Commodity", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinnerTerminal.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "Select Terminal", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinnerStack.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "Select Stack", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtBag.getText().length() == 0) {
                        Toast.makeText(mContext, "Enter Bags", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtWeight.getText().length() == 0) {
                        Toast.makeText(mContext, "Enter Weight", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtVehicleNo.getText().length() == 0) {
                        Toast.makeText(mContext, "Enter Vehicle No.", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinnerKanta.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "Select Kanta", Toast.LENGTH_SHORT).show();
                    } else if (binding.edtKantaParchiNo.getText().length() == 0) {
                        Toast.makeText(mContext, "Enter Kanta Parchi Number", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinnerLabourContractor.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "Select Contractor", Toast.LENGTH_SHORT).show();
                    } else if (fileTruck == null) {
                        Toast.makeText(mContext, "Select Truck Photo", Toast.LENGTH_SHORT).show();
                    } else if (fileKanta == null) {
                        Toast.makeText(mContext, "Select Kanta Parchi", Toast.LENGTH_SHORT).show();
                    } else if (binding.spinnerSplitDelivery.getSelectedItemPosition() == 0) {
                        Toast.makeText(mContext, "Select Split Delivery", Toast.LENGTH_SHORT).show();
                    } else {
                        offlineFastCase();
                    }
                }
            }
        });
    }

    private void getWarehouse() {
        apiService.getWarehouseData().enqueue(new NetworkCallback<ResponseWarehouse>(getActivity()) {
            @Override
            protected void onSuccess(ResponseWarehouse body) {

                commudityData = body.getCommodity_list();
                terminalData = body.getWarehouseData();
                kantaData = body.getDharam_kanta();
                contractorList = body.getContractorLists();

                for (int i = 0; i < body.getCommodity_list().size(); i++) {
                    CommudityName.add(body.getCommodity_list().get(i).getCategory());
                }

                for (int i = 0; i < body.getWarehouseData().size(); i++) {
                    terminalName.add(body.getWarehouseData().get(i).getName());
                }

                for (int i = 0; i < body.getDharam_kanta().size(); i++) {
                    kantaName.add(body.getDharam_kanta().get(i).getName());
                }

                for (int i = 0; i < body.getContractorLists().size(); i++) {
                    labourContractorName.add(body.getContractorLists().get(i).getContractor_name());
                }

                setCommoditySpinner(CommudityName);
                setTerminalSpinner(terminalName);
                setKantaSpinner(kantaName);
                setContractorSpinner(labourContractorName);

            }
        });
    }

    private void getStack() {

        apiService.getStack(terminalID, commudityID).enqueue(new NetworkCallback<ResponseStackData>(getActivity()) {
            @Override
            protected void onSuccess(ResponseStackData body) {

                stackData = body.getData();
                for (int i = 0; i < body.getData().size(); i++) {
                    StackName.add(body.getData().get(i).getStack_number());
                }
                setStackSpinner(StackName);
            }
        });
    }

    private void setCommoditySpinner(List<String> CommudityName) {

        // commodity listing
        SpinnerCommudityAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, CommudityName) {
            //By using this method we will define how
            // the text appears before clicking a spinner
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            //By using this method we will define
            //how the listview appears after clicking a spinner
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnerCommudityAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerCommudity.setAdapter(SpinnerCommudityAdapter);
        binding.spinnerCommudity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    commudityID = commudityData.get(position - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void setTerminalSpinner(List<String> terminalName) {

        // commodity listing
        SpinnerTerminalAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, terminalName) {
            //By using this method we will define how
            // the text appears before clicking a spinner
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            //By using this method we will define
            //how the listview appears after clicking a spinner
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnerTerminalAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerTerminal.setAdapter(SpinnerTerminalAdapter);
        binding.spinnerTerminal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    terminalID = terminalData.get(position - 1).getId();
                    getStack();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void setStackSpinner(List<String> stackName) {

        // commodity listing
        SpinnerStackAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, stackName) {
            //By using this method we will define how
            // the text appears before clicking a spinner
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            //By using this method we will define
            //how the listview appears after clicking a spinner
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };

        SpinnerStackAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerStack.setAdapter(SpinnerStackAdapter);
        binding.spinnerStack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    stackID = stackData.get(position - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void setKantaSpinner(List<String> terminalName) {
        // commodity listing
        SpinnerKantaAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, terminalName) {
            //By using this method we will define how
            // the text appears before clicking a spinner
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            //By using this method we will define
            //how the listview appears after clicking a spinner
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };

        SpinnerKantaAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerKanta.setAdapter(SpinnerKantaAdapter);
        binding.spinnerKanta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    kantaID = kantaData.get(position - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void setContractorSpinner(List<String> terminalName) {

        // commodity listing
        SpinnerContractorAdapter = new ArrayAdapter<String>(this, R.layout.multiline_spinner_item, terminalName) {
            //By using this method we will define how
            // the text appears before clicking a spinner
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }

            //By using this method we will define
            //how the listview appears after clicking a spinner
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(Color.parseColor("#05000000"));
                ((TextView) v).setTextColor(Color.parseColor("#000000"));
                return v;
            }
        };
        SpinnerContractorAdapter.setDropDownViewResource(R.layout.multiline_spinner_dropdown_item);
        // Set Adapter in the spinner
        binding.spinnerLabourContractor.setAdapter(SpinnerContractorAdapter);
        binding.spinnerLabourContractor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // selected item in the list
                if (position != 0) {
                    labourContractorID = contractorList.get(position - 1).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //    Log.d("bbbbbbb", "aa");
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_CAMERA_PICTURE) {

                    //    Uri imageUri = data.getData();
                    File camFile;
                    if (camUri != null) {
                        //                    Log.d("bbbbbbb", "bb");
                        if (IMAGE.equals("TRUCK")) {
                            camFile = new File(camUri.getPath());
                            fileTruck = new File(compressImage(camUri.getPath().toString()));
                            Uri uri = Uri.fromFile(fileTruck);
                            binding.imgTruck.setImageURI(uri);
                        } else if (IMAGE.equals("KANTA")) {
                            camFile = new File(camUri.getPath());
                            fileKanta = new File(compressImage(camUri.getPath().toString()));
                            Uri uri = Uri.fromFile(fileKanta);
                            binding.imgFirstKantaParchi.setImageURI(uri);
                        }
                    } else {
                        //                    Log.d("bbbbbbb", "cc");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserData(String phone) {
        if (Utility.isNetworkAvailable(this)) {
            apiService.getUserName(phone).enqueue(new NetworkCallback<ResponseUserData>(getActivity()) {
                @Override
                protected void onSuccess(ResponseUserData body) {
                    userData = body;
                    if (body.getData() != null) {
                        binding.edtUsername.setText(body.getData().getName());
                    } else {
                        Toast.makeText(mContext, body.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Utility.showAlertDialog(this, this.getString(R.string.alert), this.getString(R.string.no_internet_connection));
        }
    }

    private void offlineFastCase() {

        String truck_file = "";
        String kanta_parchi = "";

        if (fileTruck != null) {
            truck_file = "" + Utility.transferImageToBase64(fileTruck);
        }

        if (fileKanta != null) {
            kanta_parchi = "" + Utility.transferImageToBase64(fileKanta);
        }

        if (Utility.isNetworkAvailable(this)) {
            apiService.offlineFastCase(new RequestOfflineCaseData(userData.getData().getUser_id(), commudityID, binding.etPhone.getText().toString(), binding.etToken.getText().toString(), terminalID, stackID, binding.edtBag.getText().toString(), binding.edtWeight.getText().toString(), binding.edtVehicleNo.getText().toString(), kantaID, binding.edtKantaParchiNo.getText().toString(), labourContractorID, binding.spinnerSplitDelivery.getSelectedItem().toString(), "", truck_file, kanta_parchi)).enqueue(new NetworkCallback<BaseResponse>(getActivity()) {
                @Override
                protected void onSuccess(BaseResponse body) {

                    Toast.makeText(mContext, body.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Utility.showAlertDialog(this, this.getString(R.string.alert), this.getString(R.string.no_internet_connection));
        }
    }

    private void onlineFastCase() {

        String truck_file = "";
        String kanta_parchi = "";

        if (fileTruck != null) {
            truck_file = "" + Utility.transferImageToBase64(fileTruck);
        }

        if (fileKanta != null) {
            kanta_parchi = "" + Utility.transferImageToBase64(fileKanta);
        }

        if (Utility.isNetworkAvailable(this)) {
            apiService.offlineFastCase(new RequestOfflineCaseData(userData.getData().getUser_id(), commudityID, binding.etPhone.getText().toString(), binding.etToken.getText().toString(), terminalID, stackID, binding.edtBag.getText().toString(), binding.edtWeight.getText().toString(), binding.edtVehicleNo.getText().toString(), kantaID, binding.edtKantaParchiNo.getText().toString(), labourContractorID, binding.spinnerSplitDelivery.getSelectedItem().toString(), binding.edtEnterOtp.getText().toString(), truck_file, kanta_parchi)).enqueue(new NetworkCallback<BaseResponse>(getActivity()) {
                @Override
                protected void onSuccess(BaseResponse body) {

                    //        Toast.makeText(mContext, body.getMessage(), Toast.LENGTH_SHORT).show();
                    Utility.showAlertDialog(mContext, getString(R.string.alert), body.getMessage(), new Utility.AlertCallback() {
                        @Override
                        public void callback() {
                            finish();
                        }
                    });

                }
            });
        } else {
            Utility.showAlertDialog(this, this.getString(R.string.alert), this.getString(R.string.no_internet_connection));
        }
    }

    private void sendOtpFastCase(String token) {
        if (Utility.isNetworkAvailable(this)) {
            apiService.sendOtpFastCase(token).enqueue(new NetworkCallback<ResponseSendOtp>(getActivity()) {
                @Override
                protected void onSuccess(ResponseSendOtp body) {

                    if (!body.getUser_id().isEmpty()) {
                        binding.edtUsername.setText(body.getUser_name());
                        binding.lytVerifyOtp.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(mContext, body.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            Utility.showAlertDialog(this, this.getString(R.string.alert), this.getString(R.string.no_internet_connection));
        }
    }

    private void verifyOtpFastCase(String token, String otp) {
        if (Utility.isNetworkAvailable(this)) {
            apiService.verifyOtpFastCase(token, otp).enqueue(new NetworkCallback<VerifyOtpFastcase>(getActivity()) {
                @Override
                protected void onSuccess(VerifyOtpFastcase body) {

                    if (body.getData() != null && body.getData().equals("1")) {

                        VERIFY = "1";
                        binding.edtEnterOtp.setVisibility(View.GONE);
                        binding.lytVerifyOtp.setVisibility(View.GONE);
                    } else {
                        binding.edtEnterOtp.setText("");
                        Toast.makeText(mContext, body.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Utility.showAlertDialog(this, this.getString(R.string.alert), this.getString(R.string.no_internet_connection));
        }
    }

    private void onImageClicked() {
      /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > 22) {
                askForPermissions(PERMISSIONS,
                        REQUEST_CAMERA_PERMISSIONS);
            } else {
                startCameraForPic();
            }
        } else {
            startCameraForPic();
        }*/
        if (requestPermissions()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_CODE
            );
        }
    }

    private Boolean requestPermissions() {
        int cameraPermission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writeExternalPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
        int readExternalPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );
        return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalPermission == PackageManager.PERMISSION_GRANTED
                && readExternalPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void startCamera() {
        File camFile = Utility.getOutputMediaFile(mContext, "img");
        if (camFile.exists()) {
            camFile.delete();
        }
        camFile = Utility.getOutputMediaFile(mContext, "img");
        camUri = Uri.fromFile(camFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(camFile));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", camFile));
        if (Build.VERSION.SDK_INT >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivityForResult(intent, REQUEST_CAMERA_PICTURE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("imageUri", camUri);
        outState.putString("VERIFY", VERIFY);
    }

//    protected void onSaveInstanceState(Bundle icicle) {
//        super.onSaveInstanceState(icicle);
//        if (camUri != null)
//            icicle.putString("param", IMAGE);
//    }

}