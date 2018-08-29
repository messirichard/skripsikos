<?php

include_once(APPPATH . '/controllers/api/REST_Controller.php');

class Employee_api extends REST_Controller
{
	public function __construct()
	{
		parent::__construct();
		$this->load->model('Employee_model');
		$this->load->model('Users_model');
		$this->load->model('NewEmployeeToken_model');
	}
	
	public function index()
	{
		$this->middleware(['admin']);
		$user = $this->getUserData();	
		
		$employees = $this->Employee_model->get_by_admin_id($user['id']);

		foreach ($employees as $employee) {
			$user = $this->Users_model->get_by_id($employee->id_user);
			$employee->user = $user;
		}

		return $this->response(200, $employees);
	}

	public function set_to_employee()
	{
		$this->middleware(['admin']);
		$admin = $this->getUserData();

		$userID = $this->input->post('id_user');

		$this->db->insert('employee', [
			'id_admin' => $admin['id'],
			'id_user'	=> $userID,
		]);

		if (!$this->ion_auth->in_group(2, $userID)) {
			$this->ion_auth->remove_from_group(3, $userID);
			$this->ion_auth->add_to_group(2, $userID);

			return $this->response(200, ['role' => 'karyawan']);
		}

		return $this->response(400, 'Bad Request');
	}

	public function get_by_user_id()
	{
		return $this->response(200, $this);
	}

	public function set_to_user()
	{
		$this->middleware(['admin']);
		$admin = $this->getUserData();

		$userID = $this->input->post('id_user');

		$this->Employee_model->delete_by_user_id($userID);

		if (!$this->ion_auth->in_group(3, $userID)) {
			$this->ion_auth->remove_from_group(2, $userID);
			$this->ion_auth->add_to_group(3, $userID);

			return $this->response(200, ['role' => 'user']);
		}

		return $this->response(400, 'Bad Request');
	}

    public function store()
    {
        $this->middleware(['user']);
        $user = $this->getUserData();

		$expiredTime = date("Y-m-d H:i:s",strtotime(date("Y-m-d H:i:s")." +10 minute"));

        $result = $this->db->insert('new_employee_tokens', array(
			'id_user' => $user['id'],
			'token' => $this->randomString(16),
			'expired_at' => $expiredTime,
		));
		
		$this->delete_expired_token();

		if ($result === FALSE) {
			$this->response(400, 'Bad Request');
		} else {
			$this->response(200, 'Success');
		}
    }

    public function redeem($token)
    {
		$query = $this->db->select('*')
						->where('token', $token)
						->limit(1)
						->get('new_employee_tokens');

		return $this->response(200, $query->result());
	}
	
	public function delete_expired_token()
	{
		$now = date("Y-m-d H:i:s");
		$this->db->where("DATE_FORMAT(expired_at,'%Y-%m-%d %H:%i:%s') <", "'".$now."'", false);
		$this->db->delete('new_employee_tokens');

		return $this->db->affected_rows();
	}



  public function add_employe()
    {
        
   

      
   $admin_id = $this->input->post('admin_id');

        $username = $this->input->post('username');
		$password = $this->input->post('password');
		$email = $this->input->post('email');
		$additional_data = array(
								'first_name' => $this->input->post('first_name'),
								'last_name' => $this->input->post('last_name'),
                                
                                  'nik' => $this->input->post('nik'),
                                'pekerjaan' => $this->input->post('pekerjaan'),
								);
		$group = array('3'); // Sets user to member.
        $create = $this->ion_auth->register($username, $password, $email, $additional_data, $group);
		if ($create) {

             
        	$additional_datas = array(
								'id_admin' => $admin_id,
								'id_user' => $create,
                                   
								);
        	  $result = $this->db->insert('employee', $additional_datas );


            return $this->response(200, $this->Users_model->get_by_id($create));
        } else {
            return $this->response(400, "Bad request");
        }
    }


}