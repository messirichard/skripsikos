<?php

if (!defined('BASEPATH'))
    exit('No direct script access allowed');

class Transactions_model extends CI_Model
{

    public $table = 'transactions_in';
    public $id = 'transaction_id';
    public $order = 'DESC';
    public $user_id = 'user_id';

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
        return $this->db->get($this->table)->row();
    }

    function get_by_id_kamar($id)
    {
        $this->db->where('id_kamar', $id);
        return $this->db->get($this->table)->result();
    }
    
    // get total rows
    function total_rows($q = NULL) {
        $this->db->like('transaction_id', $q);
	$this->db->or_like('employee_id', $q);
	$this->db->or_like('user_id', $q);
	$this->db->or_like('name', $q);
	$this->db->or_like('laundry_packet_id', $q);
	$this->db->or_like('status_laundry', $q);
	$this->db->or_like('weight_total', $q);
	$this->db->or_like('laundry_qty', $q);
	$this->db->or_like('price', $q);
	$this->db->or_like('retreived_at', $q);
	$this->db->or_like('taken_out_at', $q);
	$this->db->from($this->table);
        return $this->db->count_all_results();
    }

    // get data with limit and search
    function get_limit_data($limit, $start = 0, $q = NULL) {
        $this->db->order_by($this->id, $this->order);
        $this->db->like('transaction_id', $q);
	$this->db->or_like('employee_id', $q);
	$this->db->or_like('user_id', $q);
	$this->db->or_like('name', $q);
	$this->db->or_like('laundry_packet_id', $q);
	$this->db->or_like('status_laundry', $q);
	$this->db->or_like('weight_total', $q);
	$this->db->or_like('laundry_qty', $q);
	$this->db->or_like('price', $q);
	$this->db->or_like('retreived_at', $q);
	$this->db->or_like('taken_out_at', $q);
	$this->db->limit($limit, $start);
        return $this->db->get($this->table)->result();
    }

    // insert data
    function insert($data)
    {
        $this->db->insert($this->table, $data);

    }

    // update data
    function update($id, $data)
    {
        $this->db->where($this->id, $id);
        $this->db->update($this->table, $data);
        if ($this->db->affected_rows() > 0)
        {
            return 1;
        }

        return 0;
    }

    // delete data
    function delete($id)
    {
        $this->db->where($this->id, $id);
        $this->db->delete($this->table);
    }

    function get_user_transactions($userid)
    {
        $this->db->select('*');
        $this->db->join('laundry_packets', 'laundry_packets.laundry_packet_id = transactions_in.laundry_packet_id');
        $this->db->where('user_id', $userid);
        $this->db->order_by('retreived_at', 'DESC');
        $query = $this->db->get($this->table);
        return $query->result();
    }

}

/* End of file Transactions_model.php */
/* Location: ./application/models/Transactions_model.php */
/* Please DO NOT modify this information : */
/* Generated by Harviacode Codeigniter CRUD Generator 2018-03-31 19:06:12 */
/* http://harviacode.com */