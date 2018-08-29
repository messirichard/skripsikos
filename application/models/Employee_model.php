<?php


class Employee_model extends CI_Model
{
	public $table = 'employee';
	public $id = 'id_employee';
	public $indekos_id = 'id_indekos';
	public $admin_id = 'id_admin';
	public $user_id = 'id_user';
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
		return $this->db->get($this->table)->row_array();
    }
    
    function get_by_admin_id($id)
	{
		$this->db->where($this->admin_id, $id);
		return $this->db->get($this->table)->result();
    }
    
    function get_by_user_id($id)
    {
        $this->db->where($this->user_id, $id);
		return $this->db->get($this->table)->row_array();
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

	function delete_by_user_id($id)
	{
		$this->db->where("id_user", $id);
		$this->db->delete($this->table);
		return ($this->db->affected_rows() > 0);
	}
}
