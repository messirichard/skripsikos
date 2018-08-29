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

		 $this->load->model('Users_model');

		 $this->load->model('Kamar_model');
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



		$user_id = $this->input->post('user_id');

		if ($kamar['book_expired_at'] < date('Y-m-d H:i:s') || $kamar['status'] == 0 || $order['transaction_id'] == null) {		
		
			$this->Order_model->delete_by_id_kamar($kamar['id_kamar']);

			$this->db->insert('orders', [
				'id_indekos' => $idIndekos,
				'id_kamar' => $idKamar,
				'amount'	=> $kamar['harga'],
				'status'	=> 'pending',
				'id_user'	=> $user_id
			]);

			$orderID = $this->db->insert_id();



  
        $user_id = $this->input->post('id_user');
      $user_detials =   $this->Users_model->get_by_id($user_id);
/*
$gfhdsf =  $this->Users_model->sendnot('Order Success',$user_detials['regid'],'Your order is success.');
*/

  //notification to admin
 $admin_details =   $this->Users_model->get_by_id('8');

$gfhdsf =  $this->Users_model->sendnot('New Order',$admin_details['regid'],'You have a new order./'.$orderID);





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
	
	public function get_order_kamar()
	{
	   
	 echo $idOrder = $this->input->post('id_kamar');
		
	$order = $this->Order_model->get_by_id_kamar($idOrder);

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


 $statuso = $this->input->post('status');
 if($statuso=='paid'){
   $user_id = $this->input->post('id_user');
      $user_detials =   $this->Users_model->get_by_id($user_id);


  //notification to admin
 $admin_details =   $this->Users_model->get_by_id('8');

$gfhdsf =  $this->Users_model->sendnot('New Payment',$admin_details['regid'],$user_detials['username'].' telah melakukan pembayaran');

}



		$data = $this->Order_model->get_by_id($idOrder);

		return ($update) ? $this->response(200, $data) : $this->response(400, "Bad Request");
	}




   function get_all_orders(){

 $dataorder = $this->Order_model->get_all();
 


  foreach ($dataorder as $key => $value) {


   if($value->id_kamar!=''){
    //kamar name 
     $user_detialsr =   $this->Kamar_model->get_by_idall($value->id_kamar);
  	  $dataorder[$key]->kamarname = $user_detialsr['no_kamar'];
  	}


 if($value->id_indekos){
  	   $user_detials =   $this->Indekos_model->get_by_id($value->id_indekos);
  	  $dataorder[$key]->indekosname = $user_detials['nama'];
  	}
  
    
  	 $user_detials =   $this->Users_model->get_by_id($value->id_user);
  	 /* $dataorder[$key]->userinfo = $user_detials;*/
  	   $dataorder[$key]->username = $user_detials['username'];
  	    $dataorder[$key]->userid = $user_detials['id'];


  	      $dataorder[$key]->email = $user_detials['email'];
  	    $dataorder[$key]->first_name = $user_detials['first_name'];
  	      $dataorder[$key]->last_name = $user_detials['last_name'];
  	    $dataorder[$key]->nik = $user_detials['nik'];
  	      $dataorder[$key]->pekerjaan = $user_detials['pekerjaan'];
  	    

  	


  	  	
    
  }





return  $this->response(200, $dataorder);
  
   }



//updtae  order status

 function order_status_update(){
//approve refuse 
$idOrder = $this->input->post('id_order');
$status = $this->input->post('status');


$msg = $this->input->post('msg');

     $orderdata = $this->Order_model->get_by_id($idOrder);

 $userid = $orderdata['id_user'];
      $user_detials =   $this->Users_model->get_by_id($userid);


   
//     print_r($orderdata);

   $orderdata['id_kamar'];

 $strr = strtotime("now");
 $updatedata = array('status'=>$status,'admin_action_time'=>$strr,'msg'=>$msg);



     $this->Order_model->update($idOrder, $updatedata);

      if($status=='refuse'){

      $datakamarup = array('status'=>0);
      $titlenot = "Order Refused"; 
      $messagenot =  "Mohon Maaf ".$user_detials['username'].", Permintaan Sewa Kos yang Anda ajukan ditolak./".$idOrder;

      }else{
 $datakamarup = array('status'=>2);
 $titlenot = "Order Approved";
  $messagenot =  "".$user_detials['username'].", Anda telah berhasil tergabung di dalam kos ini./".$idOrder;
      }

    $checkorr =    $this->Kamar_model->update($orderdata['id_kamar'], $datakamarup);



 //notification to user

  

$gfhdsf =  $this->Users_model->sendnot($titlenot,$user_detials['regid'],$messagenot);



if($checkorr){
$result_array = array('result'=>'success');
return  $this->response(200, $result_array);
}else{
	$result_array = array('result'=>'error');
	return  $this->response(200, $result_array);
}





 }





 function get_order_status(){
$kamar_id = $this->input->post('kamar_id');
$user_id = $this->input->post('user_id');

 $this->db->select("*");
        $this->db->from("orders");
        $this->db->where(array('id_kamar'=>$kamar_id,'id_user'=>$user_id));
        $this->db->order_by("order_id","desc");
        $orderdata = $this->db->get()->row_array();
   

   echo json_encode($orderdata);

   /*return  $this->response(200, $orderdata);*/



   //status pending again 

  $all_order = $this->Order_model->get_all();

   $currentss = strtotime("now");
  foreach ($all_order as $key => $value) {
  	
  	if($value->status=='refuse'){

   $orderid =  $value->order_id;
   $ordert =  $value->admin_action_time;
   if($ordert!=''){
   $yomorr = $ordert + 86400;




   if($currentss>$yomorr){

  $data = array('status'=>'pending');
     $this->Order_model->update($orderid, $data);


  
   
		    }
		  }
		  
		}


  }




 }




function get_single_order(){
$orderid = $this->input->post('orderid');
 

  $trytrgfg=  $this->Order_model->get_by_id($orderid);


  echo json_encode($trytrgfg);
}



function add_schedule(){
	$datasc = array(
		'reminder_date'=>strtotime($this->input->post('reminder_date')),
		'indekos_id'=>$this->input->post('indekos_id'),
		'indekos_name'=>$this->input->post('indekos_name'),
		'strtotime'=>strtotime("now"),
		'user_id'=>$this->input->post('user_id')
	);

 
 $uyrtier =     $this->db->select('*')->from('admin_setting')->where('indekos_id',$datasc['indekos_id'])->get()->row_array();

 


  if($datasc['reminder_date']>$uyrtier['deadline']){
$datastc['result'] =  "error";
   $datastc['msg'] ="Schedule can not be ahead from deadline";

  	return  $this->response(400, $datastc);

  }



  $dsgfdsh =  $this->db->insert('schedule',$datasc);

   if($dsgfdsh){
$datasc['result'] = "successfully";
      $datasc['id'] = $dsgfdsh;

	return  $this->response(200, $datasc);
  
   }else{
$datasc['result'] = "error";
   

	return  $this->response(400, $datasc);
   }

}




  
   function send_schedule_notification(){
$allsche= $this->db->select('*')->from('schedule')->get()->result_array();


foreach ($allsche  as $key => $value) {
	$cdata= strtotime("now");

    
  $uyrtier =     $this->db->select('*')->from('admin_setting')->where('indekos_id',$value['indekos_id'])->get()->row_array();

  $deadline = date('d-m-Y',$uyrtier['deadline']);


	if($cdata>$value['reminder_date']){

    $orderbyindekos = $this->Order_model->get_allby_indekos_id($value['indekos_id']);
  
   

    foreach ($orderbyindekos as $keya => $valuea) {

    		if( $valuea['status']=='approve' ){
    	 $admin_details =   $this->Users_model->get_by_id($valuea['id_user']);

       if($value['status']==0){
       $gfhdsf =  $this->Users_model->sendnot('Payment Deadline',$admin_details['regid'],'Mohon melakukan pembayaran sebelum '.$deadline);
     
      $upurr = $this->db->where('id',$value['indekos_id'])->update('schedule',array('status'=>1));
     }
       

         }
    }

	}else{

   $orderbyindekos = $this->Order_model->get_allby_indekos_id($value['indekos_id']);
  
    

    foreach ($orderbyindekos as $keya => $valuea) {

    		if( $valuea['status']=='approve' ){
    	 $admin_details =   $this->Users_model->get_by_id($valuea['id_user']);

 
  if($value['status']==0){
       $gfhdsf =  $this->Users_model->sendnot('Payment Deadline',$admin_details['regid'],'Anda telah melewati tanggal pembayaran kos. Mohon untuk segera untuk melakukan pembayaran ');
    $upurr = $this->db->where('id',$value['indekos_id'])->update('schedule',array('status'=>1));
     }
       

         }
    }
	}
   



}

   }




	public function show_by_kamar_id()
	{
	     $id = $this->input->post('id');
	    
		$gdsfjs = $this->db->select('*')->where('id_kamar',$id)->from('orders')->get()->result_array();
		return $this->response(200, $gdsfjs);
	}



public function order_kamar()
	{
 
 $orderid = $this->input->post('orderid');
 

  $trytrgfg=  $this->Order_model->get_by_id($orderid);

     $kamarar  =    $trytrgfg['id_kamar'];
     

          $gsdfds =   $this->Kamar_model->get_by_id($kamarar);

          foreach ($trytrgfg as $key => $value) {
           $trytrgfg['order_'.$key]=$value; 
           unset($trytrgfg[$key]);
          }

/*  $newarray = array();
          $newarray = array_merge($gsdfds,$trytrgfg);*/

      
      echo json_encode($gsdfds);


	}







}

