package edu.kiet.www.epoque2017.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import edu.kiet.www.epoque2017.Models.RegisterCancelPOJO;
import edu.kiet.www.epoque2017.R;
import edu.kiet.www.epoque2017.Requests.RegisterGroupDeptRequest;
import edu.kiet.www.epoque2017.Requests.RegisterGroupInstRequest;
import edu.kiet.www.epoque2017.Splash.SplashActivity;
import edu.kiet.www.epoque2017.networking.ServiceGenerator;
import edu.kiet.www.epoque2017.ui.coloredSnackBar;
import edu.kiet.www.epoque2017.util.DbHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sooraj on 21-02-2017.
 */

public class RegisterGroupDialogFragment extends DialogFragment {
    public static RegisterGroupDialogFragment newinstance(Context context,String type,String min,String max,String event_id)
    {
        RegisterGroupDialogFragment ob=new RegisterGroupDialogFragment();
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        bundle.putString("min",min);
        bundle.putString("max",max);
        bundle.putString("event_id",event_id);
        ob.setArguments(bundle);
        return ob;
    }
    EditText teamName,dynamic;int min,max;
    Button add; Boolean wantToClose;
    String type;
    int counter;
    ProgressDialog progressDialog;
    String steamName="",members="";
    LinearLayout linearLayout;
    View view;
    public Dialog onCreateDialog( Bundle savedInstanceState)
    {
        view=getActivity().getLayoutInflater().inflate(R.layout.register_group_dialog_fragment,null);
        teamName=(EditText)view.findViewById(R.id.edittext1);
        add=(Button)view.findViewById(R.id.add);
        linearLayout=(LinearLayout)view.findViewById(R.id.linear);
        type=getArguments().getString("type").toLowerCase().trim();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        min=Integer.parseInt(getArguments().getString("min"));
        max=Integer.parseInt(getArguments().getString("max"));
        counter=max-1;
        Log.e("MAx",Integer.toString(max));
        for(int i=1;i<min;i++)
        {
            dynamic = new EditText(getContext());
            dynamic.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dynamic.setHint("Library Id");
            dynamic.setTextColor(getResources().getColor(R.color.black));
            dynamic.setId(counter);
            linearLayout.addView(dynamic);
            counter--;
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter > 0){
                    if (counter != max - 1) {
                        if (!((TextView) linearLayout.findViewById(counter + 1)).getText().toString().trim().equals("")) {

                            dynamic = new EditText(getContext());
                            dynamic.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            dynamic.setHint("Library Id");
                            dynamic.setTextColor(getResources().getColor(R.color.black));
                            dynamic.setId(counter);
                            linearLayout.addView(dynamic);
                            counter--;
                            Log.e("counter", Integer.toString(counter));
                        } else {
                            Toast.makeText(getContext(), "Please input Previous entries first", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        dynamic = new EditText(getContext());
                        dynamic.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        dynamic.setHint("Library Id");
                        dynamic.setTextColor(getResources().getColor(R.color.black));
                        dynamic.setId(counter);
                        linearLayout.addView(dynamic);
                        counter--;
                        Log.e("counter", Integer.toString(counter));
                    }
                }

                else
                {
                    add.setEnabled(false);
                    Toast.makeText(getContext(), "Maximum Limit Reached", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Max Participations:"+Integer.toString(max)+" \n Min Participations:"+Integer.toString(min))
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        Dialog dialog=builder.create();
        return dialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    wantToClose = false;
                    function();
                    if (wantToClose) {
                        dismiss();

                    }
                }
            });
        }
    }
    public void function()
    {
        steamName=teamName.getText().toString().trim();
        if(steamName.isEmpty()){
            teamName.setError("Team name cannot be blank");
            return;
        }
        Log.e("j",Integer.toString(max-1-counter));
        for(int j=max-1;j>counter;j--)
        {
            Log.e("wert",j+""+counter);
            if(!((TextView) linearLayout.findViewById(j)).getText().toString().trim().equals("")) {
                if (j == max-1)
                    members = members + ((TextView) view.findViewById(j)).getText().toString().trim();
                else
                    members = members + "," + ((TextView) view.findViewById(j)).getText().toString().trim();
                Log.e("hello",((TextView) view.findViewById(j)).getText().toString().trim());
            }
            Log.e("members", members);
        }
        Log.e("memb",members);
        String bearer= DbHandler.getString(getContext(),"bearer","");
        if(type.equals("i"))
        {
            if(!members.trim().isEmpty()) {
                progressDialog.show();
                RegisterGroupInstRequest request = ServiceGenerator.createService(RegisterGroupInstRequest.class, bearer);
                Call<RegisterCancelPOJO> call = request.request(steamName, getArguments().getString("event_id"), members);
                call.enqueue(new Callback<RegisterCancelPOJO>() {
                    @Override
                    public void onResponse(Call<RegisterCancelPOJO> call, Response<RegisterCancelPOJO> response) {
                        progressDialog.dismiss();
                        if (response.body().getError()) {
                            DbHandler.unsetSession(getContext(), "isForcedLoggedOut");
                            startActivity(new Intent(getContext(), SplashActivity.class));
                            Toast.makeText(getContext(), "Session Expired", Toast.LENGTH_LONG).show();
                            getActivity().finishAffinity();

                        } else {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            if (response.body().getStatus().trim().equalsIgnoreCase("success")) {
                                getActivity().finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterCancelPOJO> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else if(type.equals("d"))
        {
            if(!members.trim().isEmpty()) {
                progressDialog.show();
                RegisterGroupDeptRequest request = ServiceGenerator.createService(RegisterGroupDeptRequest.class, bearer);
                Call<RegisterCancelPOJO> call = request.request(steamName, getArguments().getString("event_id"), members);
                call.enqueue(new Callback<RegisterCancelPOJO>() {
                    @Override
                    public void onResponse(Call<RegisterCancelPOJO> call, Response<RegisterCancelPOJO> response) {
                        progressDialog.dismiss();

                        if (response.body().getError()) {
                            startActivity(new Intent(getContext(), SplashActivity.class));
                            Toast.makeText(getContext(), "Session Expired", Toast.LENGTH_LONG).show();
                            DbHandler.unsetSession(getContext(), "isForcedLoggedOut");
                            getActivity().finishAffinity();

                        } else {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            if (response.body().getStatus().trim().equalsIgnoreCase("success")) {
                                getActivity().finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterCancelPOJO> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
