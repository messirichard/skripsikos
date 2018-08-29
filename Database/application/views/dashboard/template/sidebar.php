<!-- Left side column. contains the sidebar -->
<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- Sidebar user panel -->
        <div class="user-panel">
            <div class="pull-left image">
                <img src="<?php echo base_url('assets/AdminLTE-2.0.5/dist/img/user2-160x160.jpg') ?>" class="img-circle" alt="User Image" />
            </div>
            <div class="pull-left info">
                <p><?php echo $user->first_name . " " . $user->last_name ?></p>
                <a href="#"><?php echo strtoupper($user_group->name)?></a>
            </div>
        </div>
        <!-- search form -->
        <form action="#" method="get" class="sidebar-form">
            <div class="input-group">
                <input type="text" name="q" class="form-control" placeholder="Search..."/>
                <span class="input-group-btn">
                    <button type='submit' name='seach' id='search-btn' class="btn btn-flat"><i class="fa fa-search"></i></button>
                </span>
            </div>
        </form>
        <!-- /.search form -->
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
            <li class="header">MAIN NAVIGATION</li>
            <?php if($this->ion_auth->in_group('admin')):?>
            <li>
                <a href="<?= site_url('dashboard') ?>">
                    <i class="fa fa-dashboard"></i> <span>Dashboard</span>
                </a>
            </li>
            <?php endif;?>
            <li>
                <a href="<?= site_url('users') ?>">
                    <i class="fa fa-dashboard"></i> <span>Karyawan</span>
                </a>
            </li>

            <li>
                <a href="<?= site_url('users') ?>">
                    <i class="fa fa-dashboard"></i> <span>Users</span>
                </a>
            </li>

            <li>
                <a href="<?= site_url('users') ?>">
                    <i class="fa fa-dashboard"></i> <span>Transaksi</span>
                </a>
            </li>

            <li>
                <a href="<?= site_url('kos') ?>">
                    <i class="fa fa-dashboard"></i> <span>Kamar Kos</span>
                </a>
            </li>

            <li class="header">SETTINGS</li>
            <li>
                <a href="#">
                    <i class="fa fa-th"></i> <span>Account Settings</span>
                </a>
            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>

<!-- =============================================== -->

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">