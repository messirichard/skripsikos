<?php

include_once (APPPATH.'/controllers/api/REST_Controller.php');

class Users_api extends REST_Controller
{


    public function __construct()
    {
        parent::__construct();
        $this->load->model('Users_model');
        $this->load->model('Employee_model');
        $this->load->model('Users_groups_model');
        $this->load->model('Indekos_model');


        $this->load->library('Firebase');
        $this->load->library('Push');
    }

    public function profile()
    {
        $this->middleware(['members']);
        return $this->response(200, $this->getUserData());
    }

    public function set_device_token()
    {
        $this->middleware(['members']);
        $user = $this->getUserData();
        $accessToken = $this->input->post('device_token');
        $result = $this->Users_model->updateDeviceToken($user['id'], $accessToken);

        return $this->response(200, $user['id']);
    }


    public function login()
    {
        $identity = $this->input->post('username', TRUE);
        $password = $this->input->post('password');

         $regid = $this->input->post('regid');

        $userID = $this->Users_model->get_id_by_username($identity);
        $userData = $this->Users_model->get_by_id($userID);

        if ($this->ion_auth->hash_password_db($userID, $password) !== TRUE)
        {
            self::show_error('Unauthorized', 401);
        }

        
         $setregid = $this->Users_model->update($userID, ['regid'=>$regid]);

        $setToken = $this->Users_model->update($userID, ['access_token' => $this->randomString(16)]);



        if (!$setToken) {
            self::show_error('Unauthorized', 401);
        }

        if ($this->ion_auth->in_group(2, $userID)) {
            
            $userWithRole = array_merge($this->Users_model->get_by_id($userID), $this->Users_groups_model->get_by_user_id($userID));

            return $this->response(200, array_merge($userWithRole, $this->Employee_model->get_by_user_id($userID)));

        }

        
        /* if ($this->ion_auth->in_group(3, $userID)) {
            
            $userWithRole = array_merge($this->Users_model->get_by_id($userID), $this->Users_groups_model->get_by_user_id($userID));

            return $this->response(200, array_merge($userWithRole, $this->Employee_model->get_by_user_id($userID)));

        }*/





        return $this->response(200, array_merge($this->Users_model->get_by_id($userID), $this->Users_groups_model->get_by_user_id($userID)));
    }

    public function change_password()
    {
        $this->middleware(['members']);
        $oldPassword = $this->input->post('old_password', TRUE);
        $newPassword = $this->input->post('new_password', TRUE);
        $user = $this->getUserData();

        if ($this->ion_auth->hash_password_db($user['id'], $oldPassword) !== TRUE || count($user) == 0 )
        {
            self::show_error('Unauthorized', 401);
        }

        $hashed_new_password  = $this->ion_auth->hash_password($newPassword, "");
        $data = array(
            'password' => $hashed_new_password);

        if ($this->Users_model->update($user['id'], $data)) {
            return $this->response(200, [
                'message' => 'Success',
                'status' => 200
            ]);
        }

        show_error('Failed', 400);
    }


    public function do_upload()
    {
        $config['upload_path']          = './uploads/';
        $config['allowed_types']        = 'gif|jpg|png';
        $config['max_size']             = 100;
        $config['max_width']            = 1024;
        $config['max_height']           = 768;

        $this->load->library('upload', $config);

        if ( ! $this->upload->do_upload('userfile'))
        {
            $error = array('error' => $this->upload->display_errors());

            $this->load->view('upload_form', $error);
        }
        else
        {
            $data = array('upload_data' => $this->upload->data());

            $this->load->view('upload_success', $data);
        }
    }

    public function register()
    {

    $inderanssid = $this->input->post('indekos_random');
          $checkrandm =  $this->Indekos_model->get_by_randid($inderanssid);
 

     $indekosid =  $checkrandm['id_indekos'];

      if(count($checkrandm)==0){

        return $this->response(400, "Indekosn Random id not matched");


      }


        $username = $this->input->post('username');
		$password = $this->input->post('password');
		$email = $this->input->post('email');
		$additional_data = array(
								'first_name' => $this->input->post('first_name'),
								'last_name' => $this->input->post('last_name'),
                                'indekos_random' => $this->input->post('indekos_random'),
                                'nik' => $this->input->post('nik'),
                                'pekerjaan' => $this->input->post('pekerjaan'),
								);
		$group = array('3'); // Sets user to member.
        $create = $this->ion_auth->register($username, $password, $email, $additional_data, $group);
		if ($create) {

             
         $dataarray = array('id_user'=>$create);

             $this->Indekos_model->update($indekosid, $dataarray);

            return $this->response(200, $this->Users_model->get_by_id($create));
        } else {
            return $this->response(400, "Bad request");
        }
    }



 


function checknott(){
 $admin_details =   $this->Users_model->get_by_id('8');

$gfhdsf =  $this->Users_model->sendnot('New Order',$admin_details['regid'],'You have a new order.');


  print($gfhdsf);

}


function admin_setting(){


 $indekos_id =  $this->input->post('indekos_id');
 $deadline =  strtotime($this->input->post('deadline'));


  $uyrtiertyree =  $this->db->where('indekos_id',$indekos_id)->from('admin_setting')->count_all_results();

if($uyrtiertyree==0){

    $datarr =  array('indekos_id'=>$indekos_id,'deadline'=>$deadline);
     $this->db->insert("admin_setting",$datarr);
}

 if($deadline!=''){
 $admin_details =   array('deadline'=> $deadline);

 
 $this->db->where('indekos_id',$indekos_id)->update("admin_setting",$admin_details);
 }


  $uyrtier =     $this->db->select('*')->from('admin_setting')->where('indekos_id',$indekos_id)->get()->row_array();

 $uyrtier['deadline'] = date('d-m-Y',$uyrtier['deadline']);

    echo json_encode($uyrtier);

}


}