<?php


class Indekos_model extends CI_Model
{
	public $table = 'indekos';
	public $id = 'id_indekos';
	public $order = 'DESC';

	function __construct()
	{
		parent::__construct();
	}

	// get all
	function get_all()
	{
		$this->db->order_by($this->id, $this->order);
		return $this->db->get($this->table)->result();
	}

	// get data by id
	function get_by_id($id)
	{
		$this->db->where($this->id, $id);
		return $this->db->get($this->table)->row_array();
	}

	function get_by_user_id($id)
	{
		$this->db->where('id_user', $id);
		return $this->db->get($this->table)->result();
	} 


	// update data
	function update($id, $data)
	{
		$this->db->where($this->id, $id);
		$this->db->update($this->table, $data);
		return ($this->db->affected_rows() > 0);
	}

	// delete data
	function delete($id)
	{
		$this->db->where($this->id, $id);
		$this->db->delete($this->table);
		return ($this->db->affected_rows() > 0);
	}






// get data by id
	function get_by_randid($id)
	{


    $wgerere  = array('random_id'=>$id,'id_user'=>0);

       $this->db->select("*");
        $this->db->from($this->table);
        $this->db->where( $wgerere);
        return  $this->db->get()->row_array();
     $this->db->last_query();


	}


}
