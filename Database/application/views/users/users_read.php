<section class='content'>
    <div class='box'>
        <div class='box-header with-border'>
            <h2 style="margin-top:0px">Detail User</h2>

        </div>
        <div class='box-body'>

            <div class="row">
                <div class="col-md-12">
                    <table class="table">
                        <tr>
                            <td>Username</td>
                            <td><?php echo $username; ?></td>
                        </tr>
                        <tr>
                            <td>Email</td>
                            <td><?php echo $email; ?></td>
                        </tr>
                        <tr>
                            <td>First Name</td>
                            <td><?php echo $first_name; ?></td>
                        </tr>
                        <tr>
                            <td>Last Name</td>
                            <td><?php echo $last_name; ?></td>
                        </tr>
                        <tr>
                            <td>Phone</td>
                            <td><?php echo $phone; ?></td>
                        </tr>
                        <tr>
                            <td>Address</td>
                            <td><?php echo $address; ?></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><a href="<?php echo site_url('users') ?>" class="btn btn-default">Cancel</a></td>
                        </tr>
                    </table>
                </div>
             
            </div>
        </div>
    </div>
</section>