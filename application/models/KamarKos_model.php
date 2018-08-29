<?php


class KamarKos_model extends CI_Model
{
	public $table = 'kamar';
	public $id = 'id_kamar';
	public $indekos_id = 'id_indekos';
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

	function get_by_indekos_id($id)
	{
		$this->db->where($this->indekos_id, $id);
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
}
