<?php

include_once(APPPATH . '/controllers/api/REST_Controller.php');

class Indekos_api extends REST_Controller
{
	public function __construct()
	{
		parent::__construct();
		$this->load->model('Indekos_model');
	}



	public function index()
	{
		$userId = $this->input->post('id_user');
		$data = $this->Indekos_model->get_all();

		foreach ($data as $key => $value) {
			
		 $uyrtier =   $this->db->select('*')->from('admin_setting')->where('indekos_id',$value->id_indekos)->get()->row_array();
	if($uyrtier['deadline']){
		 $data[$key]->deadline = date('d-m-Y',$uyrtier['deadline']);
		}else{
 $data[$key]->deadline = '';
		}

	}
		return $this->response(200, $data);
	}




	public function show($id)
	{
		$data = $this->Indekos_model->get_by_id($id);
		return $this->response(200, $data);
	}

	public function store() 
	{
		/*$this->middleware(['admin']);
		$user = $this->getUserData();*/

		$config['upload_path'] = UPLOADS;
	/*	$config['allowed_types'] = 'gif|jpg|png|jpeg|bmp|PNG|JPEG|JPG';*/
	    $config['allowed_types'] = '*';

		$config['max_size'] = 2000;
	/*	$new_name = time() . '-' . $user['username'];*/
	    $new_name = time();
		$config['file_name'] = $new_name;

		$this->upload->initialize($config);

		if (!$this->upload->do_upload('foto')) {
			$error = array('error' => $this->upload->display_errors());

			self::show_error($error, 400);
		} else {

			$uploadData = $this->upload->data();

			 $randm = mt_rand();

			$result = $this->db->insert('indekos', array(
				'nama' => $this->input->post('nama'),
				'alamat' => $this->input->post('alamat'),
				'gender' => $this->input->post('gender'),
				'kota' => $this->input->post('kota'),
				'fasilitas_umum' => $this->input->post('fasilitas_umum'),
				'foto' => UPLOADS."/".$new_name.$uploadData['file_ext'],
				/*'id_user' => $user['id'],*/
				'id_user' => 0,
				'random_id' => $randm,
			));

			if ($result === FALSE) {
				$this->response(400, 'Bad Request');
			}

			$this->response(200, $result);
		}
	}


	public function update($id)
	{
		$this->middleware(['admin']);
		$user = $this->getUserData();

		$config['upload_path'] = UPLOADS;
		$config['allowed_types'] = 'gif|jpg|png|jpeg|bmp|PNG|JPEG|JPG';
		$config['max_size'] = 2000;
		$new_name = time() . '-' . $user['username'];
		$config['file_name'] = $new_name;

		$this->upload->initialize($config);

		if (!empty($_FILES['foto']['name'])){
			if (!$this->upload->do_upload('foto')) {
				$error = array('error' => $this->upload->display_errors());
				self::show_error($error, 400);
			} else {
				if(is_file($config['upload_path']))
				{
					chmod($config['upload_path'], 777); ## this should change the permissions
				}

				$uploadData = $this->upload->data();
				$previousData = $this->Indekos_model->get_by_id($id);
				unlink($previousData['foto']);

				$data = [
					'foto' => UPLOADS."/".$new_name.$uploadData['file_ext'],
					'nama' => $this->input->post('nama'),
					'alamat' => $this->input->post('alamat'),
					'gender' => $this->input->post('gender'),
					'kota' => $this->input->post('kota'),
					'fasilitas_umum' => $this->input->post('fasilitas_umum'),
					'id_user' => $user['id']
				];
			}
		}else{
			$data = array(
				'nama' => $this->input->post('nama'),
				'alamat' => $this->input->post('alamat'),
				'gender' => $this->input->post('gender'),
				'kota' => $this->input->post('kota'),
				'fasilitas_umum' => $this->input->post('fasilitas_umum'),
				'id_user' => $user['id']
			);
		}
	

		$result = $this->Indekos_model->update($id, $data);

		$indekos = $this->Indekos_model->get_by_id($id);
		$this->response(200, $indekos);
	
	}

	public function update_foto($id)
	{
		$config['upload_path'] = UPLOADS;
		$config['allowed_types'] = 'gif|jpg|png';
		$config['max_size'] = 2000;
		$new_name = time() . '-' . $user['username'];
		$config['file_name'] = $new_name;

		$this->upload->initialize($config);

		if (!$this->upload->do_upload('foto')) {
			$error = array('error' => $this->upload->display_errors());

			self::show_error($error, 400);
		} else {
			if(is_file($config['upload_path']))
			{
				chmod($config['upload_path'], 777); ## this should change the permissions
			}

			$uploadData = $this->upload->data();

			$previousData = $this->Indekos_model->get_by_id($id);

			unlink($previousData['foto']);


			$data = [
				'foto' => UPLOADS."/".$new_name.$uploadData['file_ext'],

			];

			$result = $this->Indekos_model->update($id, $data);

			if ($result === FALSE) {
				$this->response(400, 'Bad Request');
			}

			$this->response(200, $result);
		}
	}

	public function destroy($id)
	{
		$result = $this->Indekos_model->delete($id);

		if (!$result) {
			$this->response(400, 'Bad Request');
		} else {
			$this->response(200, 'Success');
		}
	}




public function get_user_boarding()
	{
		$userId = $this->input->post('id_user');
		$data = $this->Indekos_model->get_by_user_id($userId);
		return $this->response(200, $data);
	}




}
