package com.apnagodam.staff.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.DisplayMetrics;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.apnagodam.staff.Base.BaseActivity;
import com.apnagodam.staff.R;
import com.apnagodam.staff.databinding.BillPrintBinding;
import com.apnagodam.staff.module.SpotSellDealTrackPojo;
import com.apnagodam.staff.printer.AsyncBluetoothEscPosPrint;
import com.apnagodam.staff.printer.AsyncEscPosPrinter;
import com.apnagodam.staff.printer.AsyncUsbEscPosPrint;
import com.apnagodam.staff.utils.Utility;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContractFormBillPrintClass extends BaseActivity<BillPrintBinding> {
    SpotSellDealTrackPojo.Datum order;
   /*==============================================================================================
    ======================================BLUETOOTH PART============================================
    ==============================================================================================*/

    public static final int PERMISSION_BLUETOOTH = 1;

    public void printBluetooth() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, ContractFormBillPrintClass.PERMISSION_BLUETOOTH);
        } else {
            // this.printIt(BluetoothPrintersConnections.selectFirstPaired());
            new AsyncBluetoothEscPosPrint(this).execute(this.getAsyncEscPosPrinter(null));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivityAndClear(SpotDealTrackListActivity.class);
                }
            }, 5000);
        }
    }

   /* @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService.getAllStockListDetails(bookingID).enqueue(new NetworkCallback<GetAllOrderDetailsResponse>(getActivity()) {
            @Override
            protected void onSuccess(GetAllOrderDetailsResponse body) {
                order = body.getOrder();
            }
        printBluetooth();
    }*/

    @Override
    protected int getLayoutResId() {
        return R.layout.bill_print;
    }

    @Override
    protected void setUp() {
       // bookingID = intent.getExtras().getString("bookingid");
        order = (SpotSellDealTrackPojo.Datum) getIntent().getSerializableExtra("serialzable");
        printBluetooth();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case ContractFormBillPrintClass.PERMISSION_BLUETOOTH:
                    this.printBluetooth();
                    break;
            }
        }
    }


    /*==============================================================================================
    ===========================================USB PART=============================================
    ==============================================================================================*/

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ContractFormBillPrintClass.ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            // printIt(new UsbConnection(usbManager, usbDevice));
                            new AsyncUsbEscPosPrint(context)
                                    .execute(getAsyncEscPosPrinter(new UsbConnection(usbManager, usbDevice)));
                        }
                    }
                }
            }
        }
    };

    /**
     * Asynchronous printing
     */
    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        // UserDetails userDetails = SharedPreferencesRepository.getDataManagerInstance().getUser();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);
        String buyerPhone = order.getBuyer_phone().replaceAll("\\d(?=(?:\\D*\\d){4})", "*");
        String sellerPhone = order.getSeller_phone().replaceAll("\\d(?=(?:\\D*\\d){4})", "*");
        Double totalAmount =Double.parseDouble(order.getQuantity())*Double.parseDouble(order.getPrice());
        double AG_commission =(1*(totalAmount))/100;
        String roundedNumberFinalPrice = ""+ Utility.round(AG_commission, 2);

           return printer.setTextToPrint(
              /*    *//* "[L]<b>Terminal Copy</b>[R]<font size='small'></font>\n" +*//*
                           "[L]\n" +*/
                           "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.bill_company_logo, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                           "[L]\n" +
                           "[L]<b>Bid Date</b>[R]<font size='small'>" +  order.getUpdatedAt() + "</font>\n" +
                           /* "[C]<u><font size='big'>CIN :"+userDetails.getAadhar_no()+"</font></u>\n" +*/
                           "[L]<b>CIN :U63030RJ2016PTC055509</font></b>\n" +
                           /*  "[L]<b>CIN:-</b>[R]<u>U12345678565PT</u>\n" +*/
                           "[C]--------------------------------\n" +
                           "[L]<b>Contract Note No:-</b>[R]" + order.getContractId() + "\n" +
                           "[C]--------------------------------\n" +
                           "[L]<b>Buyer Name:-</b>[R]" + order.getFname() + "\n" +
                           "[L]<b>PAN No:-</b>[R]" + order.getBuyer_pancard_no() + "\n" +
                           "[L]<b>PHONE No:-</b>[R]" + buyerPhone + "\n" +
                           "[C]--------------------------------\n" +
                           "[L]<b>Seller Name:-</b>[R]" + order.getSellerName() + "\n" +
                           "[L]<b>PHONE No:-</b>[R]" + sellerPhone + "\n" +
                           "[C]--------------------------------\n" +
                           "[L]<b>Commodity:-</b>[R]" + order.getCategory() + "\n" +
                           "[L]<b>Net.Weight:-</b>[R]" + order.getQuantity() + " Qtl\n" +
                           "[L]<b>Terminal:-</b>[R]" + order.getLocation() + "  \n" +
                          /* "[L]<b>Bags:-</b>[R]" + order.getNo_of_bags() + "\n" +*/
                           "[L]<b>Selling Price:-</b>[R]" + order.getPrice() + "/Qtl.\n" +
                           "[C]--------------------------------\n" +
                           "[L]<b>Total Amount:-</b>[R]" + totalAmount + " RS\n" +
                           "[C]--------------------------------\n" +
                           "[L]<b>AG Commission:-</b>[R]" + roundedNumberFinalPrice + " RS\n" +
                           "[C]--------------------------------\n" +
                           "[R]Reg.Office:-Plot No.16[R]\n" +
                           "[R]Sector-09[R]\n" +
                           "[R]Vidhyadhar Nagar,Jaipur[R]\n" +
                           "[R]Rajasthan 302039[R]\n" +
                           "[R]E-mail:-contact@apnagodam.com[R]\n" +
                           "[C]--------------------------------\n" +
                           "[R]Customer Care:-7733901154[R]\n" +
                           "[R]landlines No:-0141-2232204[R]\n" +
                           "[C]--------------------------------\n" +
                           "[C]<font size='small'>This is a system generated contract note</font>\n" +
                           "[C]<font size='small'>hence not be signed</font>\n" +
                           "[C]--------------------------------\n" +
                           "[L]\n" +
                           "[C]<qrcode size='25'>Contact Note =" + order.getContractId() + ",ApnaGodam.in,CIN=U63030RJ2016PTC055509,Buyer Name=" + order.getFname() + ",Seller Name= " + order.getSellerName() + ",Date=" +order.getUpdatedAt() + ",Commodity =" + order.getCategory() + ",Location =" + order.getLocation() + ",Net Weight=" + order.getQuantity() + ",Price=" + order.getPrice() /*+ ",bags=" + order.getNo_of_bags()*/ + ",Final Amount:-" + totalAmount + " RS/</qrcode>\n" +
                           "[L]\n" +
                           "[C]<font size='small'>The Farmer Produce Trade and commerce</font>\n" +
                           "[C]<font size='small'>Ordinance,2020</font>\n"
           );

    }

    /*   *//**
     * Asynchronous printing
     *//*
    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 32);

        return printer.setTextToPrint(
                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.mipmap.app_logo, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                        "[L]\n" +
                        "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
                        "[C]<font size='small'>" + format.format(new Date()) + "</font>\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99e\n" +
                        "[L]  + Size : S\n" +
                        "[L]\n" +
                        "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
                        "[L]  + Size : 57/58\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[R]TOTAL PRICE :[R]34.98e\n" +
                        "[R]TAX :[R]4.23e\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<font size='tall'>Customer :</font>\n" +
                        "[L]Raymond DUPONT\n" +
                        "[L]5 rue des girafes\n" +
                        "[L]31547 PERPETES\n" +
                        "[L]Tel : +33801201456\n" +
                        "[L]\n" +
                        "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                        "[C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>\n"
        );
    }*/
}
