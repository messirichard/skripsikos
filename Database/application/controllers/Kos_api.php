<?php

include_once(APPPATH . '/controllers/api/REST_Controller.php');


class Kos_api extends REST_Controller
{

	public function __construct()
	{
		parent::__construct();
		$this->load->model('KamarKos_model');
		$this->load->model('Indekos_model');
		$this->load->model('Order_model');
	}

	public function index()
	{
		$result = $this->KamarKos_model->get_all();
		if (!$result) {
			$this->response(400, 'Bad Request');
		} else {
			$this->response(200, $result);
		}	
	}

	public function show($id)
	{
		$result = $this->KamarKos_model->get_by_id($id);
		if (!$result) {
			$this->response(400, 'Bad Request');
		} else {
			$this->response(200, $result);
		}	
	}

	public function show_by_indekos_id($id)
	{
		$kamarKos = $this->KamarKos_model->get_by_indekos_id($id);

		$this->inspectKamarKosAvailability($kamarKos);		 

		$kamarKos = $this->KamarKos_model->get_by_indekos_id($id);
		return $this->response(200, $kamarKos);
	}


	public function store()
	{
		$result = $this->db->insert('kamar', array(
			'id_indekos' => $this->input->post('id_indekos'),
			'no_kamar' => $this->input->post('no_kamar'),
			'lantai_ke' => $this->input->post('lantai_ke'),
			'ukuran' => $this->input->post('ukuran'),
			'harga' => $this->input->post('harga'),
			'fasilitas' => $this->input->post('fasilitas'),
			'kwh' => $this->input->post('kwh'),
			'status' => 0,
		));

		if ($result === FALSE) {
			$this->response(400, 'Bad Request');
		} else {
			$this->response(200, 'Success');
		}
	}


	public function update($id)
	{
		$data = array(
			'id_indekos' => $this->input->post('id_indekos'),
			'no_kamar' => $this->input->post('no_kamar'),
			'lantai_ke' => $this->input->post('lantai_ke'),
			'ukuran' => $this->input->post('ukuran'),
			'harga' => $this->input->post('harga'),
			'fasilitas' => $this->input->post('fasilitas'),
			'kwh' => $this->input->post('kwh'),
		);

		$result = $this->KamarKos_model->update($id, $data);

		if (!$result) {
			$this->response(400, 'Bad Request');
		} else {
			$this->response(200, 'Success');
		}
	}

	public function inspectKamarKosAvailability($kamarKos)
	{
		$server_key = 'VT-server-rlDs-ZfahTwuT2TywuNF3ZCs';
		$params = array('server_key' => $server_key, 'production' => false);
		$this->load->library('veritrans');
		$this->veritrans->config($params);

		foreach ($kamarKos as $kamar) {
				
			if ($kamar->book_expired_at < date('Y-m-d H:i:s')) {	
				$this->KamarKos_model->update($kamar->id_kamar, [
					'status' => 0,
					'book_expired_at' => null
				]);
			}

			if ($kamar->status == 2) {
				$order = $this->Order_model->get_by_id_kamar($kamar->id_kamar);
				
				if (isset($order['transaction_id'])) {
					$status = $this->veritrans->status($order['transaction_id']);
					if ((int) $status->status_code == 200) {
						$this->Order_model->update($order['order_id'], [
							'status' => "paid"
						]);

						$this->KamarKos_model->update($kamar->id_kamar, [
							'status' => 1,
							'book_expired_at' => date('Y-m-d H:i:s',strtotime('+30 day',strtotime($kamar->book_expired_at)))
						]);
					}
				}
			}
		}
	}

	public function destroy($id)
	{

		$result = $this->KamarKos_model->delete($id);

		if (!$result) {
			$this->response(400, 'Bad Request');
		} else {
			$this->response(200, 'Success');
		}
	}



	public function create_order()
	{		
		$idIndekos = $this->input->post('id_indekos');
		$indekos = $this->Indekos_model->get_by_id($idIndekos);

		$idKamar = $this->input->post('id_kamar');
		$kamar = $this->KamarKos_model->get_by_id($idKamar);
		$order = $this->Order_model->get_by_id_kamar($idKamar);

		if ($kamar['book_expired_at'] < date('Y-m-d H:i:s') || $kamar['status'] == 0 || $order['transaction_id'] == null) {		
		
			$this->Order_model->delete_by_id_kamar($kamar['id_kamar']);

			$this->db->insert('orders', [
				'id_indekos' => $idIndekos,
				'id_kamar' => $idKamar,
				'amount'	=> $kamar['harga'],
				'status'	=> 'pending'
			]);

			$orderID = $this->db->insert_id();

			$this->KamarKos_model->update($idKamar, [
				'status' => 2,
				'book_expired_at' => date('Y-m-d H:i:s',strtotime('+12 hour',strtotime(date("Y-m-d H:i:s"))))
			]);

			$data = [
				"transaction_details" => [
					"order_id" => $orderID,
					"gross_amount" => $kamar['harga']
				],
				"item_details" => [
					"id" => 1,
					"price" => $kamar['harga'],
					"quantity" => 1,
					"name" => $indekos['nama'] . " - " . $kamar['no_kamar'] 
				]
			];

			return $this->response(200, $data);
		} else {
			return $this->response(400, ["message" => "Booked"]);
		}
	}

	public function get_order()
	{
		$idOrder = $this->input->post('id_order');
		$order = $this->Order_model->get_by_id($idOrder);

		$indekos = $this->Indekos_model->get_by_id($order['id_indekos']);

		$kamar = $this->KamarKos_model->get_by_id($order['id_kamar']);

		$data = [
			"transaction_details" => [
				"order_id" => $idOrder,
				"gross_amount" => $kamar['harga']
			],
			"item_details" => [
				"id" => 1,
				"price" => $kamar['harga'],
				"quantity" => 1,
				"name" => $indekos['nama'] . " - " . $kamar['no_kamar'] 
			]
		];

		return $this->response(200, $data);
	}

	public function after_transaction()
	{
		$idOrder = $this->input->post('id_order');
		$update = $this->Order_model->update($idOrder, [
			'transaction_id' => $this->input->post('transaction_id'),
			'id_user' => $this->input->post('id_user'),
			'status' => $this->input->post('status'),
			'payment_type' => $this->input->post('payment_type')
		]);

		$data = $this->Order_model->get_by_id($idOrder);

		return ($update) ? $this->response(200, $data) : $this->response(400, "Bad Request");
	}
}
